package com.example.restfulwebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


//< 'Swagger Documentation 구현 방법'강 >
@Configuration //'설정'관련 파일이기에, '@Configuration'을 붙임.
@EnableSwagger2 //'Swagger' 용도.
public class SwaggerConfig {


    //'특정 회원의 연락처 정보 객체'를 만들기
    private static final Contact DEFAULT_CONTACT = new Contact("Kenneth Lee",
            "http://www.joneconsulting.co.kr", "edowon@joneconsulting.co.kr");


    //'API Info 값의 정보 객체'를 만들기
    //'API Info'는 한 번 만들면 이후에는 변경할 필요가 없는 정보이기 때문에, 이렇게 '상수'로 만들어서 이후에 변경 가능성을 없애는 것
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Awesome API Title",
            "My User management REST API service","1.0", "urn:tos", DEFAULT_CONTACT, "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());


    //
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
            Arrays.asList("application/json", "application/xml")); //- 'application/json': 'json 타입'을 지원한다는 것 의미
                                                                   //- 'application/xml': 'xml 타입'을 지원한다는 것 의미
                                                                   //- 'Arrays.asList()': '배열'을 'List'로 바꿔주는 것


    @Bean
    public Docket api(){ //'Docket 객체'는, '메소드 api의 내부 데이터들'을 '문서화(documentation)'시켜준다! 이제,
                         //- '주소창에 'www.localhost:8088/v2/api-docs''라고 입력하면,
                         //  이 애플리케이션의 각종 api가 'JSON 형식'의 문서로 생성됨을 확인 가능.
                         //- '주소창에 'www.localhost:8088/swagger-ui.html'라고 입력하면,
                         //  이 애플리케이션의 각종 api가 'html 형식'의 문서로 생성됨을 확인 가능.



        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
