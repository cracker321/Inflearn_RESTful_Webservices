package com.example.restfulwebservice.helloworld;

import com.example.restfulwebservice.HelloWorldBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

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


}
