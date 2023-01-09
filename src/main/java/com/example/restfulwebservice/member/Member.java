package com.example.restfulwebservice.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

//@JsonFilter("UserInfo") //- 여기에 붙게 되는 '필터'은 이제 '컨트롤러'와 'service'에서 사용될 것임.
                        //- "UserInfo": 필터 이름.
                        // '컨트롤러' 또는 'service'에서 '어떤 bean'을 대상으로 사용될 필터인지' 그 필터 이름임.
                        //  여기서는 'AdminUser 객체의 메소드 retrieveUser'에서 사용했음. 해당 부분 확인하기.
@NoArgsConstructor //'UserV2 객체'가 'User 객체'로부터 상속받으려면, 'UserV2 객체'가 만들어질 때 '부모 객체인 User의 객체'를 
                   //참고해서 만들어지는데, 그러하기 위해서는 'User 객체와 UserV2 객체 각각의 내부에 기본 생성자가 반드시 존재'해야만 한다!
                   //따라서, '@NoArgsConstructor'를 통해 '디폴트 생성자'를 생성해준다!
                   //아니면, 'User 객체' 내부에 '디폴트 생성자' 로직 직접 작성해줘도 됨. 'public User () {}'.
@ApiModel(description = "사용자의 상세 정보를 위한 도메인 객체. All details about the user.") //
//@Table(name = "USERS") //'h2 DB'에는 '테이블 USERS'로 생성된다!. 'h2 DB'에서는 'User가 예약어'이기 때문에 서로 겹쳐서 오류나는 것임.
@Entity
public class Member {


    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.") //'Postman 통신'할 때 전달되는 해당 JSON 데이터의 입력 조건임
    @ApiModelProperty(notes = "사용자의 이름을 입력해주세요.")
    private String name;

    @Past //현재 등록된 회원이 가입했던 날짜는, '미래'가 아니라 당연히 '과거'이기 때문에, '과거 데이터'만 사용 가능하다는 조건 부여함
    @ApiModelProperty(notes = "사용자의 등록일을 입력해주세요.")
    private Date joinDate;



    //< 'Response 데이터 제어를 위한 Filtering'강 05:30~ >
    //외부에 노출시키고 싶지 않은 데이터인 'password'와 'ssn(주민번호 같은 것)'
    //'@JsonIgnore'를 추가시켜줌으로써,
    //JSON 통신을 통해서 사용자로부터 전달받은 데이터들 중에서, 보안 상 컨트롤러로 보내고 싶지 않은 데이터를,
    //그에 해당하는 아래 필드 위에 일일이 '@JsonIgnore'를 붙여준다!
    //그러면, 이제 '사용자로부터 User 엔티티 정보'가 넘어올 때, 아래 두 데이터는 '컨트롤러 UserController'로 넘어가지 않게 된다!
    //또한, 따라서, '서버'가 '사용자'에게 응답 리턴값 전달할 때, '@JsonIgnore가 붙은 필드의 데이터'는 '제외되고',
    //따라서, 포스트맨으로 응답 데이터 확인할 때, 해당 필드 데이터는 사용자에게 리턴값으로 전달되지 않는다!
    //@JsonIgnore
    @ApiModelProperty(notes = "사용자의 비밀번호를 입력해주세요.")
    private String password;

    //@JsonIgnore
    @ApiModelProperty(notes = "사용자의 비밀번호를 입력해주세요.")
    private String ssn; //이렇게 해당 필드에 일일이 '@JsonIgnore'를 붙여줘도 되고, 아니면 아예 저 위에 '클래스 블록'으로 
                        //'@JsonIgnoreProperties' 적고, 괄호 안에 내가 보안 상 가리고 싶은 필드를 넣어줘도 된다!


    //< '게시물 관리를 위한 Post Entity 추가와 초기 데이터 생성'강 >
    //- 'Post 객체(N. 주인 객체) : 'Member 객체(1. 종속 객체)'

    //cf)
    //N:1 관계 : 말 그대로 N명의 회원이 1개의 팀에 소속될 수 있다는 뜻
    //단방향 매핑: 회원 객체만이 팀 객체의 내부데이터를 알고 싶어함
    //양방향 매핑: 회원 객체와 팀 객체 모두 서로의 객체의 네부데이터를 알고 싶어함
    @OneToMany(mappedBy = "member") //'주인 객체 Post의 필드 member'
    private List<Post> posts = new ArrayList<Post>(); //'현재 회원'이 '몇 개의 게시글들을 작성했는지'를 확인하기 위한 필드



    //< '게시물 관리를 위한 Post Entity 추가와 초기 데이터 생성'강 10:00~ > ??????????????. 이거 질문게시판에 질문하기
    //아래 생성자는 왜 생성해주는지..? '서비스 MemberService'의
//    static{
//        //'User 객체 속 모든 필드들'에 해당하는 값들을 아래에 차례차례 넣어줘야 한다! 안그러면 에러난다!
//        members.add(new Member(1, "Kenneth", new Date(), "pass1", "701010-1111111"));
//        members.add(new Member(2, "Alice", new Date(), "pass2", "801010-2222222"));
//        members.add(new Member(3, "Elena", new Date(), "pass3", "901010-1111111"));
//    }
    //위 부분 때문에 그러는 것인데,
    //이미 여기 'Member 객체'에 'AllArgsConstructor'와 'NoArgsConstructor' 다 했는데
    //왜 또 여기 아래에 이렇게 해줘야 에러가 풀리는건지...
    public Member(int id, String name, Date joinDate, String password, String ssn) {

        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
