package com.example.restfulwebservice.helloworld;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data //@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 다 포함되어있음.
      //@RequiredArgsConstructor와 @AllArgsConstructor와 @NoArgsConstructor 차이점 확인하기!
@AllArgsConstructor //'현재 클래스 안에 선언된 모든 필드들을 매개변수로 사용한 생성자'를 만들어주는 롬복
public class HelloWorldBean {

    private String message;

//    < 위 '@Data'에 아래 Getter, Setter들 다 포함되어 있기 때문에, 아래처럼 Getter, Setter 직접 타이핑 안해줘도 된다! >
//    public String getMessage(){
//        return this.message;
//    }
//
//    public void setMessage(String certainMessage){
//        this.message = certainMessage;
//    }

//    < 위 '@AllArgsConstructor'가 선언되었기 때문에, 아래처럼 직접 타이핑 안해줘도 된다! >
//    - 'Setting' --> '검색창에 annotation 입력' --> 'enable annotation processing'
//    이렇게 해줘야, 만약 '@AllArgsConstructor'를 입력한 후에, 실수로 어노테이션에 저 어노테이션에 해당하는 중복되는 생성자를 만들었을 경우
//    '서버 실행'시켜줬을 때, '중복되는 코드, 기능, 내용'이라고 에러 발생시켜줌. 즉, 보다 정확한 코드를 위해서임!
//
//    public HelloWorldBean(String message){
//          this.message = message
//    }

}
