package com.example.restfulwebservice.user;


//< Postman 에러코드 종류 >
//2XX : 정상응답
//4XX : Client
//- 사용자가 존재하지 않는 서버 내부 리소스를 요청했다던가,
//- 클라이언트가 적절한 권한을 가지고 있지 않다던가,
//- 작성되지 않은 메소드를 호출했다던가 등
//5XX : Server
//- 서버가 사용하고 있는 외부 리소스와 연결이 안 됐다던가 등

//< 임의 처리 Exception 에러 코드 >
//- '에러코드 5XX'으로 전달해도 되지만, 사실은 '서버 내부 리소스 User'가 존재하지 않는 것이기 때문에

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //HTTP Status Code 제어를 위한 Exception Handling강 09:10~
                                      //이렇게 선언하면, 앞으로 이 예외 클래스는 '5XX 에러가 아니라, 400 또는 404 에러'로
                                      //클라이언트에게 전달됨됨public class UserNotFoundException extends RuntimeException { //이 예외는 'throw'를 던지지 않고,
                                      // 'ReuntimeException'을 발생시키도록 함.
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message); //생성자는, 부모클래스로부터 전달받은 '변수 message'를 사용함
    }
}
