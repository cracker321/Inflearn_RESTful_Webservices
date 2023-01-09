package com.example.restfulwebservice.exception;
//< AOP >
//- 로깅정보, 로그인정보 등 '공통적으로 현재 프로젝트의 개별 컨트롤러에서 각각 항상 실행시켜주어야 하는 비즈니스로직' 등을 AOP에 담음
//- '예외 처리 핸들러 클래스'도 AOP에 해당되어 공통적으로 처리해줘야 하는 기능임.

import com.example.restfulwebservice.user.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

//'Spring의 AOP를 이용한 Exception Handling'강 03:50~
@ControllerAdvice //서버 실행되어, 그 과정으로 '모든 컨트롤러들'이 차례차례 실행될 때, 이 '@ControllerAdvice'를 달고 있는 빈(객체)이 실행됨
                  //만약, 서버 실행 과정에서 에러가 발생한다면, 여기 이 클래스에서 등록시킨 '에러 메소드'가 바로 실행됨
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
                                                                //'JVM 내장 클래스인 ResponseEntityExceptionHandler'를
                                                                //상속받게 함

    @ExceptionHandler(Exception.class) //- 아래 메소드가 'ExceptionHandler'로 사용될 수 있도록 어노테이션 추가함.
                                       //- '(Exception.class)': 어떠한 에러 처리를 할 것인지를 지정
                                       //- 어떠한 컨트롤러가 서버 실행될 때 실행된다 하더라도, '현재 클래스'가 실행될 것이고,
                                       //현재 클래스 안에서 예외가 발생하게 된다면, 아래 '메소드 handleAllExcetpions'가 실행되는 로직.
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        //'Spring의 AOP를 이용한 Exception Handling'강 05:00~
        //'모든 예외'를 처리해주기 위해 내가 임의로 생성한 메소드 handleAllExceptions.
        //'ResponseEntity<Object>': '사용자 객체'를 한 명씩 추가시켰을 때 리턴했던 형태의 리턴값.
        //'내장 클래스 Exception':
        //'내장 인터페이스 WebRequest': 에러가 어디서 발생했는지에 대한 정보를 '매개변수 request'로 받아오는 것.


        ExceptionResponse exceptionResponse =  //
                            new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
                                //- 'new Date()': 에러가 언제 발생했는지
                                //- 'ex.getMessage()': 에러 메시지가 무엇인지
                                //- 'request.getDescription()': 에러 정보는 무엇인지

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                                            //- 원래는 'ResponseEntity<Object>(...)'로 해야 하는데, '제네릭이 Object 객체'면
                                            //생략 가능함.
                                            //- '새로운 ResponseEntity 객체'를 만들어 위에서 만든 데이터값을 여기에 넣어 리턴해줌
                                            //'5XX 에러이기에, INTERNAL_SERVER_ERROR'로 설정해줌.

    }



    //'URL 요청'에서 들어오는 정보들 중에, 'db에 없는 사용자 정보를 요청'할 때 발생시키는 예외를 만듦
    @ExceptionHandler(MemberNotFoundException.class) //'UserNotFoundException 예외'가 발생하면, 아래 메소드가 실행되게 함.
    public final ResponseEntity<Object> handleMemberNotFoundException(Exception ex, WebRequest request){

        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }



    //'내장 추상 클래스 ResponseEntityExceptionHandler 내부의 메소드 handleMethodArgumentNotVaild'를 여기로 가져와서
    //여기서 '재정의'함. 저 위에 'ResponseEntityExceptionHandler' 글자 위에 'ctrl'누르고 마우스 왼쪽 누르면
    //해당 내장 로직으로 들어가짐. 거기서, 'ctrl+F'로 아래 메소드 찾고 복사해서 여기로 가져오는 것임.
    //'내장 추상 클래스'이기 때문에, 재정의 할 수 있음. 따라서, '오버라이딩'해야 함.
//    @Override //'유효성 체크를 위한 Validation API 사용'강 05:15~
//              //'오버라이딩'한다는 것은, '부모 클래스의 특정 부모 메소드'를 '반.드.시' '재정의하는 것'임.
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//                                    MethodArgumentNotValidException ex, //'발생한 예외 객체'
//                                    HttpHeaders headers, //'요청 리퀘스트의 헤더값'
//                                    HttpStatus status, //'HttpStatus 값'
//                                    WebRequest request) { //'요청 리퀘스트 값'
//
//        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
//                                                                    "Validation Failed", //보여줄 오류 메시지 입력함
//                                                                    ex.getBindingResult().toString()
//                                                                    );
//        //- '내가 이전에 다른 클래스 형태로 만든 ExceptionResponse 클래스'를 'new 연산자'를 활용하여 '객체로 만듦'
//        //- 'new Date': 에러가 발생한 시간('현재 시간'으로 설정함)
//        //- 'ex.getMessage': 에러 메시지
//        //-
//       return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
//    }
}





