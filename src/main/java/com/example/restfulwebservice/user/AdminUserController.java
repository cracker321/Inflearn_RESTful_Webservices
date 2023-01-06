package com.example.restfulwebservice.user;

//< Hibernate >
//- 자바에서 db와 관련된 어플리케이션을 개발하기 위해 사용하는 API
//- '자바의 객체'와 'db의 엔티티'를 매핑하기 위한 프레임워크를 제공해줌

//< HTTP 헤더 >
//- HTTP 전송에 필요한 모든 부가 정보
//  e.g) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 등..
//- 형식
//  e.g) Host: www.google.com


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RequestMapping("/admin") //아래에서 '공통된 URL'을 여기에 적어줄 수 있음
@RequiredArgsConstructor
@RestController
public class AdminUserController { //'관리자'만을 위한 컨트롤러. '일반 사용자'가 사용하는 기능보다 더 중요한 기능들을 포함하고 있음

    private UserService userService;

    public AdminUserController(UserService userService){ //생성자를 통한 의존성주입!
        this.userService = userService;
    }


    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){


        List<User> users = userService.findAll();

        //============================================================================================================

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);

        mapping.setFilters(filters);
        //============================================================================================================


        return mapping;
    }






    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id){ //- '개별 사용자'를 조회함
                                                    //- '관리자'이기에 프론트로부터 넘어오는 'User 엔티티 조회 요청'에 대해
                                                    //

        User user = userService.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }


        //'프로그래밍으로 제어하는 Filtering 방법 - 개별 사용자 조회'강 05:00~
        //- 클라이언트로부터 포스트맨 통해서 JSON 데이터(여기서는 'User 객체' 데이터) 요청 들어올 때,
        //  'User 객체에서 선언('@JsonFilter')한 필터'인 "필터명 UserInfo'를 여기서 적어주고,
        //  아래의 '필터링 속성(필드 id, name, joinDate, ssn)'에 작성해준 필드들만 DB에서 가져와서
        //  클라이언트에게 다시 전달해주는 것임!
        //- 즉, 아래 로직으로 인해, 포스트맨으로 JSON 통신할 때 이제
        //  {
        //    "id": 1,
        //    "name": "Kenneth",
        //    "joinDate": "2020-03-26T10:25:17.407+0000",
        //    "ssn": "701010-1111111"
        //  }
        // 이렇게만 클라이언트에게 전다
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);


        return mapping;
    }


    

}
