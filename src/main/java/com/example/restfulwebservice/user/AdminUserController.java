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
import org.springframework.beans.BeanUtils;
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


//=====================================================================================================================


    //1. '컨트롤러의 메소드 버전 관리법 1': 'URI Versioning'. 일반 브라우저에서 실행 가능
    //< 'URI를 이용한 REST API Version 관리'강 03:00~ >
    //@GetMapping("v1/users/{id}")  //버전 차이를 이렇게 나타낼 수도 있고,


    //2. '컨트롤러의 메소드 버전 관리법 2': 'Request Parameter Versioning'. 일반 브라우저에서 실행 가능
    //< 'Request Parameter와 Header를 이용한 API Version 관리'강 00:00~ > //버전 차이를 이렇게도 나타낼 수 있고,
    //@GetMapping(value = "/users/{id}/", params = "version=1") //- '@GetMappiing()'의 '()' 안에 지금 여기처럼 두 가지 이상의
                                                                   //  정보가 들어온다면, 'URL 경로(path)' 앞에 여기처럼
                                                                   //  'value ='를 붙여줘야 한다!!
                                                                   //  또한, 경로 끝에 반드시 '/'를 또 붙여줘야 한다!
                                                                   //- 이 방법은 '포스트맨'에서 경로에는
                                                                   //  'http://localhost:8088/admin/users/1?version=1'
                                                                   //  이라 쓰고,
                                                                   //  기본으로 'Params 탭'을 선택해두어야 한다


    //3. '컨트롤러의 메소드 버전 관리법 3': 'Request Header Versioning'. 일반 브라우저에서 실행 불가능
    //< 'Request Parameter와 Header를 이용한 API Version 관리'강 03:20~ > //버전 차이를 이렇게도 나타낼 수 있다.
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1" ) //단, 이 방법은 '포스트맨'에서 경로에는
                                                                       //'http://localhost:8088/admin/users/1'라고만 쓰고
                                                                       //'Headers 탭'에서 'KEY' 에 'X-API-VERSION'을 넣고
                                                                       //'VALUE'에는 '1'을 넣어야 한다!


    //4. '컨트롤러의 메소드 버전 관리법 4': 'Media-type Versioning'. : 일반 브라우저에서 실행 불가능
    //< 'Request Parameter와 Header를 이용한 API Version 관리'강 06:00~ > //버전 차이를 이렇게도 나타낼 수 있다.
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json" ) //단, 이 방법은 '포스트맨' 경로에
                                                                    //'http://localhost:8088/admin/users/1'라고만 쓰고
                                                                    // 'Headers 탭' 누르고,
                                                                    //거기에서 'KEY'에 'Accept'를 넣고
                                                                    //'VALUE'에는 'application/vnd.company.appv1+json'을
                                                                    //넣는다!
    public MappingJacksonValue retrieveUserV1(@PathVariable int id){ //- '개별 사용자'를 조회함
                                                    //- '관리자'이기에 프론트로부터 넘어오는 'User 엔티티 조회 요청'에 대해
                                                    //

        User user = userService.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }


        //< '프로그래밍으로 제어하는 Filtering 방법 - 개별 사용자 조회'강 05:00~ >
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


//=====================================================================================================================


    //GET /admin/users/1 --> /admin/v2/users/1

    //1. '컨트롤러의 메소드 버전 관리법 1': 'URI Versioning'. 일반 브라우저에서 실행 가능
    //< 'URI를 이용한 REST API Version 관리'강 03:00~ >
    //@GetMapping("v2/users/{id}")  //버전 차이를 이렇게 나타낼 수도 있고,


    //2. '컨트롤러의 메소드 버전 관리법 2': 'Request Parameter Versioning'. 일반 브라우저에서 실행 가능
    //< 'Request Parameter와 Header를 이용한 API Version 관리'강 00:00~ > //버전 차이를 이렇게도 나타낼 수 있고,
    //@GetMapping(value = "/users/{id}/", params = "version=2") //- '@GetMappiing()'의 '()' 안에 지금 여기처럼 두 가지 이상의
                                                                    //  정보가 들어온다면, 'URL 경로(path)' 앞에 여기처럼
                                                                    //  'value ='를 붙여줘야 한다!!
                                                                    //  또한, 경로 끝에 반드시 '/'를 또 붙여줘야 한다!
                                                                    //- 이 방법은 '포스트맨'에서 경로에는
                                                                    //  'http://localhost:8088/admin/users/1?version=2'
                                                                    //  이라 쓰고(여기서 '?'는, 뒤에 'param'이 오기 떄문에 넣음)
                                                                    //  기본으로 'Params 탭'을 선택해두어야 한다.

    //3. '컨트롤러의 메소드 버전 관리법 3': 'Request Header Versioning'. 일반 브라우저에서 실행 불가능
    //< 'Request Parameter와 Header를 이용한 API Version 관리'강 03:20~ > //버전 차이를 이렇게도 나타낼 수 있다.
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2" ) //단, 이 방법은 '포스트맨'에서 경로에는
                                                                       //'http://localhost:8088/admin/users/1'라고만 쓰고
                                                                       //'Headers 탭' 누르고,
                                                                       // 거기에서 'KEY' 에 'X-API-VERSION'을 넣고
                                                                       //'VALUE'에는 '2'을 넣어야 한다!

    //4. '컨트롤러의 메소드 버전 관리법 4': 'Media-type Versioning'. : 일반 브라우저에서 실행 불가능
    //< 'Request Parameter와 Header를 이용한 API Version 관리'강 06:00~ > //버전 차이를 이렇게도 나타낼 수 있다.
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json" ) //단, 이 방법은 '포스트맨' 경로에
                                                                    //'http://localhost:8088/admin/users/1'라고만 쓰고
                                                                    //'Headers 탭' 누르고,
                                                                    //거기에서 'KEY'에 'Accept'를 넣고
                                                                    //'VALUE'에는 'application/vnd.company.appv2+json'을
                                                                    //넣는다!
    public MappingJacksonValue retrieveUserV2(@PathVariable int id){ //- '개별 사용자'를 조회함
        //- '관리자'이기에 프론트로부터 넘어오는 'User 엔티티 조회 요청'에 대해
        //

        User user = userService.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }

        //< 'URI를 이용한 REST API Version 관리'강 06:50~ >
        //- '글자 User' --> '글자 UserV2' 로 빠르게 바꾸는 방법. '글자'만 바꾸는 것이 아니라 여기서는 'UserV2 객체'가 필요하기에,
        //  'User 객체'를 'UsreV2 객체'로 빠르게 바꾸는 방법임.

        //- 바로 위에서 'User user = userService.findOne(id)'를 통해, DB에 접근하여 'User 객체'의 정보를 먼저 조회해서 가져왔고,
        //  그 다음 아래 과정을 통해, 'User 객체의 내부 데이터'를 'User2 객체의 내부'에 '복사(옮기기)'하는 것임.
        UserV2 userV2 = new UserV2(); //'클참뉴클': 'UserV2 클래스'를 'UserV2 객체'로 만듦
        BeanUtils.copyProperties(user, userV2); //- 'JVM 내장 클래스 BeanUtils': '객체(Bean)들' 간의 관련되어 있는 여러 작업들을
                                                //                              할 수 있도록 도와주는 클래스.
                                                //                              e.g) 인스턴스 생성,
                                                //                                   두 인스턴스 간 공통 필드 있는 경우에는
                                                //                                   인스턴스 복사 가능하게 해주는 등(바로 여기서)

        //- 'UserV2 객체' 내부에는 'User 객체의 필드(데이터)'를 다 포함하고, 여기에 더해 '필드 grade'도 있음.
        userV2.setGrade("VIP"); //임시로 'VIP'를 '필드 grade'에 저장해둠. 즉, 클라이언트에게 응답 바디 보낼 때, JSON 객체에서
                                //  { ...
                                //  "grade" : "VIP"
                                //  ... }     이렇게 클라이언트로 전달해줌.


        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);


        return mapping;
    }


//=====================================================================================================================


}
