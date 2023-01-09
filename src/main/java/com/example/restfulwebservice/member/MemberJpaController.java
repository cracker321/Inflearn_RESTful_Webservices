package com.example.restfulwebservice.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;


@RequestMapping("/jpa")
@RestController
public class MemberJpaController { //'레펏 MemberRepository'를 전담으로 사용하는 컨트롤러임

    @Autowired
    private MemberRepository memberRepository;


//=====================================================================================================================


    //< 전체 사용자 목록 조회 >
    @GetMapping("/members")
    public List<Member> retrieveAllMembers(){

        return memberRepository.findAll(); //- '레펏 MemberRepository'가 '상속받은' '내장 레펏 JpaRepository'의 '내장 메소드'를 쓰는 것

    }


//=====================================================================================================================


    //< 개별 사용자 목록 조회 > + < Hateoas 적용 >
    @GetMapping("/members/{id}") //- '
    public Member retrieveMember(@PathVariable int id){

        Optional<Member> member = memberRepository.findById(id); //- '레펏 Repository'의 '내장 메소드 findById'는
                                                                 // 'Optional 타입'이기에, 이에 맞게 감싸줘야 함
                                                                 //1. 일단은, '클라이언트가 전달한 사용자 id가
        //                                                       //   존재하는지 여부'부터 파악하기

        if(!member.isPresent()) { //'DB'에 '해당 member의 id 값'이 존재하지 않는다면,
            throw new MemberNotFoundException(String.format("ID[%s]가 없습니다!", id)); //'예외 MemberNotFoundException'을
                                                                                      // 발생시켜줄거야!
        }

//        'Hateoas 내용'인데 헷갈...
//        EntityModel<Optional<Member>> entityModel = EntityModel.of(member);
//
//        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllMembers());
//
//        entityModel.add(linkTo.withRel("all-members"));
//
//        return ResponseEntity<EntityModel<Member>>.ok(entityModel);


        return member.get(); //- 'DB'에 '해당 member의 id 값'이 존재한다면, 그 member의 데이터를 가져와서 보여줄게!
                             //- 아마 'Optional 타입'의 '내장 메소드 get'인 듯..
    }


//=====================================================================================================================


    @DeleteMapping("/members/{id}")
    public void deleteMember(@PathVariable int id){

        memberRepository.deleteById(id);

        //'삭제'이기 때문에 재정의(리턴값 반환)작업 필요없다!
    }


//=====================================================================================================================


    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member){
                        //< '유효성 체크를 위한 Validation API 사용'강 03:10~ >
                        //- '@Valid':

                        //- '@RequestBody': '클라이언트가 추가하고 싶은 사용자 정보'는 당연히 'Header(HTTP메시지의 요약본)'가 아닌
                        //                  'Body(HTTP메시지의 본문)'에 들어있기에,
                        //                  '@RequestHeader'가 아닌 '@RequestBody'에 담겨져 서버로 오는 것이다!

        Member savedMember = memberRepository.save(member); //'새롭게 사용자를 추가하고', '그 추가한 정보'를 'savedMember'에 저장


        //< 'JPA를 이용한 사용자 추가와 삭제 - POST/DELETE HTTP Method'강 03:35~ >
        //- 'ServletUriComponentsBuilder': DB에 저장한 id값 데이터('변수 savedMember')를 사용자에게 '헤더'에 포함시켜 전달하는데,
        //                                 이 때, '새롭게 저장된 사용자 id값 데이터'가 DB에 제대로 저장되었는지 여부를 확인하는 페이지로
        //                                 이동할 URI를 만들어서 사용자에게 전달해주는 것.
        //                                 https://velog.io/@modsiw/Spring-Boot-ServletUriComponentBuilder
//        # 'ResponseEntity'
//        형식: return new ResponseEntity<>(HTTP바디(본문), HTTP헤더, 상태코드)
//             e.g:) return new ResponseEntity<>(dto, header, HttpStatus.OK);
//
//        # 응답 헤더(Response Header)
//        - Location: 상태코드 301, 302 일 때만 볼 수 있는 헤더로, 서버의 응답이 다른 곳에 있다고 알려주면서 해당 위치(URI)를 지정한다.
//                    300번대 응답(리다이렉션)이나 201 Created 응답일 때 어느 페이지로 이동할지를 알려주는 헤더입니다.
//                    e.g) HTTP/1.1 302 Found
//                         Location: http://...
//                         이런 응답이 왔다면 브라우저는 http://... 주소로 리다이렉트합니다.
//        - Server: 웹서버의 종류. e.g) nginix ...
//        - Age: max-age 시간 내에서 얼마나 흘렀는지 초 단위로 알려주는 값
//        - Referrer-policy: 서버 referrer 정책을 알려주는 값. e.g) origin, no-referrer, unsafe-url
//        - WWW-Authenticate: 사용자 인증이 필요한 리소스를 요구할 시, 서버가 제공하는 인증 방식
//        - Proxy-Authenticate: 요청한 서버가 프록시 서버인 경우 유저 인증을 위한 값

        //'응답 상태코드 300대(리다이렉션)' 또는 '201(Created)'일 경우, 'HTTP 응답 헤더'에 'Location'을 포함시켜 사용자에게
        //리턴해주는데, 이 때 그 'Location'에 들어가는 'URI 주소'를 생성해주는 로직!
        //아래 링크의 '개발자 도구'-->'F11'눌러서 상태코드들 확인해보면 300대 있는데, 그 부분의 'Headers 탭'눌러서 확인해보면 됨
        //https://goddaehee.tistory.com/169
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() //'현재 요청되어진 로직'을 사용한다는 뜻
                .path("{/id}") //사용자에게 '무엇을 리턴시켜줄 것인가?': '새롭게 저장된 사용자 id값'을 리턴시켜줄 것이다!
                .buildAndExpand(savedMember) //'새롭게 저장된 사용자 id값'을 '리소스'로 활용하여 '리소스 URI'를 만듦
                .toUri(); //'URI 형태'로 변환시켜줌

        return ResponseEntity.created(location).build(); //- 'HTTP 응답 헤더'를 설정하려면, 'return 'Member 객체''가 아닌,
                                                         //'return ResponseEntity 객체' 형태로 사용자에게 리턴해줘야 한다!
                                                         //- ResponseEntity를 'builder 패턴'으로 작성한 것임!
                                                         //https://thalals.tistory.com/268

    }

    //'사용자 추가 후', 클라이언트에게 상태코드를 비롯한 상태값을 전달해주기 위해 ResponseEntity를 사용함.

//=====================================================================================================================


    //< 하나의 회원이 작성한 모든 게시글들'을 조회 >
    @GetMapping("/members/{id}/posts") //e.g) '/jpa/members/90001/posts': 90001번 회원이 작성한 모든 게시글 조회
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){

        Optional<Member> member = memberRepository.findById(id);//1. 일단은, '클라이언트가 전달한 사용자 id가
                                                                   //   존재하는지 여부'부터 파악하기

        if(!member.isPresent()){
            throw new MemberNotFoundException(String.format("ID[%s]가 없습니다!", id));
        }


        return member.get().getPosts(); //- 'Member 객체의 여러 필드 데이터들 중'에서 '필드 posts'만 가져와서 반환해주기
                                        //- 'Optional 객체 타입의 메소드 get'(인 듯..?)

    }


}
