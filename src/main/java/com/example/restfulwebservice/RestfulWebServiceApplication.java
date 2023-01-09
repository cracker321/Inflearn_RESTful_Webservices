package com.example.restfulwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class RestfulWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulWebServiceApplication.class, args);
	}



	//'다국어 처리를 위한 Internationalization 구현 방법'강 01:10~
	//서버가 실행될 때, 아래 정보에 해당하는 값들이 메모리에 올라가서,
	@Bean
	public LocaleResolver localeResolver(){
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.KOREA);

		return localeResolver;
	}
}


/*
# JPA
  - Java Persistance API
  - 자바 ORM 기술에 대한 API 표준 명세
    * ORM: 객체 관계 매핑(Object Relational Mapping)
  - 자바 어플리케이션에서 관계형 DB를 사용하는 방식을 정의한 인터페이스
  - EntityManager를 통해 CRUD를 처리


# Hibernate
  - JPA의 구현체. 인터페이스를 직접 구현한 라이브러리
  - 생산성, 유지보수, 비종속성
  JPA: 인터페이스
  Hibernate: JPA의 구현체
  Spring Data JPA: JPA를 추상화한 Repository 인터페이스를 제공함

# Spring Data JPA
  - Spring Module
  - JPA를 추상화한 Repository 인터페이스를 제공함







 */