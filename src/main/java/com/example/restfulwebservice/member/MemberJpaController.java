package com.example.restfulwebservice.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
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

        ServletUriComponentsBuilder.


    }

    //'사용자 추가 후', 클라이언트에게 상태코드를 비롯한 상태값을 전달해주기 위해 ResponseEntity를 사용함.

}
