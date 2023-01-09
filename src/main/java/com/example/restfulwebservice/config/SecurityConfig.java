package com.example.restfulwebservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll(); //스프링시큐리티를 사용하되,
                                                                                       //해당 링크는 시큐리티 면제해줌
        http.csrf().disable(); //'csrf 기능'도 작동 안하게 함
        http.headers().frameOptions().disable(); //'HTTP 헤더 값들 중'에서 'frameoptions 속성'도 사용하지 않음 

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
            auth.inMemoryAuthentication()
                    .withUser("kenneth")
                    .password("{noop}test1234") //포스트맨에서 'Authorization 탭' --> 'Basic Auth' --> 
                                                //옆에 'Username과 password'에 'kenneth'와 'test1234' 입력해보라는 것
                                                //- 'noop': 인코딩 하지 않고(=어떠한 동작도 하지 않고(no operation)),
                                                //          바로 인코딩 한다는 뜻. 그러나 실무에서는 '{noop}' 안쓰고 적절한 인코딩
                                                //          알고리즘을 사용한.
                    .roles("USER"); //- 'roles("USER")': 로그인이 완료되면, 해당 사용자는 'USER' 권한을 갖게 됨
        }
    }


