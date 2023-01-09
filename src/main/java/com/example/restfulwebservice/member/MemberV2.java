package com.example.restfulwebservice.member;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 다 포함되어있음.
     //@RequiredArgsConstructor와 @AllArgsConstructor와 @NoArgsConstructor 차이점 확인하기!
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password", "ssn"}) //- JSON 통신을 통해서 프론트로부터 전달받은 데이터들 중에서,
                                                   //보안 상 컨트롤러로 보내고 싶지 않은 데이터를, 그에 해당하는 아래 필드 위에
                                                   //일일이 '@JsonIgnore'를 붙여줘도 되고,
                                                   //아니면 아예 여기 '클래스 블록'으로 '@JsonIgnoreProperties' 적고,
                                                   //괄호 안에 내가 보안 상 컨트롤러에 보내고 싶지 않은 필드를 넣어줘도 된다!
                                                   //포스트맨 결과 화면 조회에서 확인할 수 있음
//그러면, 이제 '클라이언트로부터 User 엔티티 요청'이 넘어올 때, 해당 데이터는 '컨트롤러 UserController'로 넘어가지 않게 된다!

@JsonFilter("UserInfoV2") //- 여기에 붙게 되는 '필터'은 이제 '컨트롤러'와 'service'에서 사용될 것임.
                          //- "UserInfo2": 필터 이름.
                          // '컨트롤러' 또는 'service'에서 '어떤 bean'을 대상으로 사용될 필터인지' 그 필터 이름임.
                          //  여기서는 'AdminUser 객체의 메소드 retrieveUser'에서 사용했음. 해당 부분 확인하기.
@NoArgsConstructor //'UserV2 객체'가 'User 객체'로부터 상속받으려면, 'UserV2 객체'가 만들어질 때 '부모 객체인 User의 객체'를
                   //참고해서 만들어지는데, 그러하기 위해서는 'User 객체와 UserV2 객체 각각의 내부에 기본 생성자가 반드시 존재'해야만 한다!
                   //따라서, '@NoArgsConstructor'를 통해 '디폴트 생성자'를 생성해준다!
                   //아니면, 'User 객체' 내부에 '디폴트 생성자' 로직 직접 작성해줘도 됨. 'public UserV2 () {}'.
public class MemberV2 extends Member { //'UserV2 객체'는 'User 객체'를 상속받음.
                                  //즉, 'UserV2 객체'는 그 내부 필드(데이터)로 최소한 일단
                                  //'User 객체의 내부 데이터(필드)'를 포함하고 있는 상태임.

    private String grade; //'UserV2 객체'는 'User 객체의 내부 필드(데이터)' 이외에 이 '필드 grade'를 갖음. '회원 등급'에 대한 정보임.



}
