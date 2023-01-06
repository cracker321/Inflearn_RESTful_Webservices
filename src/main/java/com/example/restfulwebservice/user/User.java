package com.example.restfulwebservice.user;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data//@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 다 포함되어있음.
     //@RequiredArgsConstructor와 @AllArgsConstructor와 @NoArgsConstructor 차이점 확인하기!
@AllArgsConstructor
@JsonIgnoreProperties(value={"password", "ssn"})
public class User {


    private Integer id;
    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.") //'Postman 통신'할 때 전달되는 해당 JSON 데이터의 입력 조건임
    private String name;
    @Past //현재 등록된 회원이 가입했던 날짜는, '미래'가 아니라 당연히 '과거'이기 때문에, '과거 데이터'만 사용 가능하다는 조건 부여함
    private Date joinDate;


    //'Response 데이터 제어를 위한 Filtering'강 05:30~
    //외부에 노출시키고 싶지 않은 데이터인 'password'와 'ssn(주민번호 같은 것)'
    //'@JsonIgnore'를 추가시켜줌으로써, JSON으로 데이터를 주고 받을 때, 이제 Postman 통신 할 때 아래 필드들은 아예 HTTP Body에서
    //보여지지 않게 된다! 즉, 정상적으로 아래 데이터들도 주고 받아지는 것은 맞지만, 보안 상 이유로 이제 Postman 조회할 때가려지게 되는 것임!
    //@JsonIgnore
    private String password;
    //@JsonIgnore
    private String ssn; //이렇게 해당 필드에 일일이 '@JsonIgnore'를 붙여줘도 되고, 아니면 아예 저 위에 '클래스 블록'으로 
                        //'@JsonIgnoreProperties' 적고, 괄호 안에 내가 보안 상 가리고 싶은 필드를 넣어줘도 된다!


}
