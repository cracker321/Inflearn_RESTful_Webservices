package com.example.restfulwebservice.user;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data//@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 다 포함되어있음.
     //@RequiredArgsConstructor와 @AllArgsConstructor와 @NoArgsConstructor 차이점 확인하기!
@AllArgsConstructor
public class User {


    private Integer id;
    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.")
    private String name;
    @Past //현재 등록된 회원이 가입했던 날짜는, '미래'가 아니라 당연히 '과거'이기 때문에, '과거 데이터'만 사용 가능하다는 조건 부여함
    private Date joinDate;
}
