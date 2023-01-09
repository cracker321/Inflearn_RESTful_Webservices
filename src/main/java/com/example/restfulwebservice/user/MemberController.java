package com.example.restfulwebservice.user;

//< Hibernate >
//- 자바에서 db와 관련된 어플리케이션을 개발하기 위해 사용하는 API
//- '자바의 객체'와 'db의 엔티티'를 매핑하기 위한 프레임워크를 제공해줌

//< HTTP 헤더 >
//- HTTP 전송에 필요한 모든 부가 정보
//  e.g) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 등..
//- 형식
//  e.g) Host: www.google.com


import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RequiredArgsConstructor
@RestController
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService){ //생성자를 통한 의존성주입!
        this.memberService = memberService;
    }

//=====================================================================================================================


    @GetMapping("/users")
    public List<Member> retrieveAllMembers(){

        //return userService.findAll(); //이거를 'ctrl + alt + v' 또는 '마우스 우클릭 -> Refactor -> Introduce Variable' 누르면
                                        //쪼개서 코드 작성할 수 있음. 즉, 바로 아래처럼 할 수 있음.
                                        //List<User> users = userService.findAll();
                                        //return users;
                                        //이렇게 써 줄 수 있다!
        return memberService.findAll();
    }


//=====================================================================================================================


    @GetMapping("/users/{id}") //'개별 사용자'를 조회
    //public User retrieveUser(@PathVariable int id){ //'Hateos 적용 전'
    public ResponseEntity<EntityModel<Member>> retrieveMember(@PathVariable int id){

        Member member = memberService.findOne(id);

        if(member == null){
            throw new MemberNotFoundException(String.format("ID[%s} not found", id));
        }

        //< 'Level3 단계의 REST API 구현을 위한 HATEOAS 적용'강 05:00~ >
        EntityModel<Member> entityModel = EntityModel.of(member); //'매개변수'로 '위에서 DB에 접근하여 검색되어진 user 객체 값'을 넣음
                                                           //('User user = userService.findOne(id);')
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllMembers());
                                                    //- 이 user 값을 반환시킬 때, 이 user가 사용할 수 있는 추가적인 정보(링크)를
                                                    //  하이퍼미디어 타입으로 넣음.
                                                    //- 'WebMvcLinkBuilder': '어떤 링크'를 추가해줄 것인지 지정하는 것.
                                                    //                       즉, 여기서는 '전체 사용자 목록 보기'로 돌아가는 것.
                                                    //                       'retrieveAllUsers()'로 돌아가는 것이기 때문.
                                                    //- 'this.getClass()).retrieveAllUsers()': 'this.getClass'가 가지고
                                                    //                  있는 데이터들 중에서, 'retrieveAllUsers()'를 연동함
        entityModel.add(linkTo.withRel("all-users")); //위에서 생성한 'WebMvcLinkBuilder 객체'에다가 '링크'를 추가시키고,
                                                      //그것을 'all-users 라는 URI명'과 연동시킴

        //즉, '라이브러리 Hateoas'는 '하나의 리소스'에서 '파생되는 여러 추가적 작업들'을 확인해볼 수 있음.
        //'Hateoas 작업 자체'는 '개발자에게는 추가적인 작업'을 요구하지만, '애플리케이션 사용하는 사용자 입장'에서는 다양한 추가 정보를
        //한 번에 얻을 수 있다는 장점이 있음.

        //return userService.findOne(id); //'Hateos 적용 전'의 리턴문
        return ResponseEntity.ok(entityModel); //'Hateos 적용 후'의 리턴문
    }


//=====================================================================================================================


    @PostMapping("/users")
    public ResponseEntity<Member> createMemeber(@Valid @RequestBody Member member){
                                        //- @RequestBody : 사용자가 작성하여 보낸 'JSON 객체에 담겨진 내용'이 넘어올 때
                                        //이를 받기 위해 여기에 컨트롤러에 작성함. 전달받고자 하는 이 데이터가
                                        //'RequestBody 형식'의 역할을 하겠다고 선언하는 것.
                                        //'RequestBody'로 'user 엔티티'를 건네줬다는 것은, 클라이언트가 서버로
                                        //'user 엔티티 객체' 내부의 필드들 'name'과 'joinDate'를 JSON으로
                                        //건네줬다는 것임. 즉, postman에 입력되는 json 정보는 예를 들어 아래와 같음.
                                        //{
                                        //    "name": "Yujong",
                                        //    "joinDate": "2020-03-26T10:25:17.407+0000"
                                        //}
                                        //'유효성 체크를 위한 Validation API 사용'강 03:10~
                                        //- @Valid:

        Member savedMember = memberService.save(member);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest() //'Http Status Code 제어'강 3:00~'
                                                                        //'새로운 URI'를 생성하는 과정임.                .path("/{id}")
                .buildAndExpand(savedMember.getId())
                .toUri();

        return ResponseEntity.created(location).build(); //'created는

    }


//=====================================================================================================================


    @DeleteMapping("/users/{id}")
    public void deleteMember(@PathVariable int id){

        Member member = memberService.deleteById(id);

        if(member == null){ //- 'url을 통해 클라이언트가 보낸 '사용자 id''에 해당하는 정보가 db에 존재하지 않는다면,
            throw new MemberNotFoundException(String.format("ID[%s] not found", id)); //'UserNotFoundException 에러'를
                                                                                    //발생시켜 준다!

        }
    }


//=====================================================================================================================

}
