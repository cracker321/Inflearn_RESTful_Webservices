package com.example.restfulwebservice.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorldController {

    @Autowired
    private MessageSource messageSource; //'message 값'을 리턴하기 위함임.


    @GetMapping("/hello-wolrd/bean")
    public HelloWorldBean helloWorldBean(){ //저 아래 '메소드 helloWoroldBean'과 메소드명이 같지만, 매개변수가 다르기에 오버로딩에 해당!

        return new HelloWorldBean("Hello World");
    }


    @GetMapping("/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name){ //만약, 메소드 본문에 'name과 다른 값'을 사용하고 싶다면,
                                                                     //'@PathVariable(value = 다른 값명)' 이렇게 해주면 된다!


        return new HelloWorldBean(String.format("Hello World, %s!", name)); //만약, 클라언트로부터 'name'에 '조유종'이라는 값이
                                                                          //들어왔다면, 출력값: 'Hello World, 조유종!'이 출력됨.
    }


    //'다국어 처리를 위한 Internationalization 구현 방법'강 05:35~
    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized(@RequestHeader(name="Accept-Language", required=false) Locale locale){
                                //- 'Locale 값'은 원래 '@RequestHeader'에 의해 전달되기 때문에, '@RequestHeader'를 붙여줌.
                                //- 우리가 전달하고자 하는 값은 'Accept-Language'임.
                                //그리고, 이 값은 '필수(required)'가 될 수 없기 때문에 'required=false'라고 설정함.
                                //- 'RequestHeader'에 'Accept-Language'가 포함되어 있지 않을 경우: 'Locale 값(=한글, 한국어)'실행
                                //- 'RequestHeader'에 'Accept-Language'가 포함되어 있을 경우:

        return messageSource.getMessage("greeting.message",null, locale);

    }

}
