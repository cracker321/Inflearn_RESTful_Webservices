package com.example.restfulwebservice.exception;

//'Spring의 AOP를 이용한 Exception Handling'강 01:00~

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor //현재 클래스의 모든 필드를 가지고 있는 생성자를 만듦
@NoArgsConstructor //매개변수가 없는 '기본 생성자'를 만듦
public class ExceptionResponse { //이 클래스는 '현재 프로젝트의 모든 컨트롤러'에서 '공통으로 사용할 수 있는 예외 클래스'임

    private Date timestamp; //예외가 발생한 시간에 대한 정보
    private String message; //예외가 발생한 메시지
    private String details; //예외에 대한 상세 정보
}
