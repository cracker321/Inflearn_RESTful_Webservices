package com.example.restfulwebservice.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


//N:1 관계 : 말 그대로 N명의 회원이 1개의 팀에 소속될 수 있다는 뜻
//단방향 매핑: 회원 객체만이 팀 객체의 내부데이터를 알고 싶어함
//양방향 매핑: 회원 객체와 팀 객체 모두 서로의 객체의 네부데이터를 알고 싶어함

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    //cf)
    //N:1 관계 : 말 그대로 N명의 회원이 1개의 팀에 소속될 수 있다는 뜻
    //단방향 매핑: 회원 객체만이 팀 객체의 내부데이터를 알고 싶어함
    //양방향 매핑: 회원 객체와 팀 객체 모두 서로의 객체의 네부데이터를 알고 싶어함

    //< '게시물 관리를 위한 Post Entity 추가와 초기 데이터 생성'강 >
    //- 'Post 객체(N. 주인 객체) : 'Member 객체(1. 종속 객체)'. 'N:1 양방향 매핑'.
    @Id
    @GeneratedValue
    private Integer id; //'현재 게시글의 번호'

    private String description; //'현재 게시글의 정보'

    @ManyToOne(fetch = FetchType.LAZY) //- 'fetch = FetchType.LAZY' : '지연 로딩'방식.
                                       //   'Member 객체'를 조회할 때, 그때마다 매 번 'Post 객체'가 같이 조회되는
                                       //   것이 아니라, 'Post 객체'가 로딩되는 시점에 그때 그때 필요한 'Member 객체'의
                                       //   정보를 가지고 오겠다는 뜻
                                       //   즉, 어딘가에서 'Post 객체'를 호출하더라도, 그때마다 매 번 
                                       //   아래 '필드 member 데이터'를 불러오지 않아도 된다는 뜻
    @JsonIgnore //아래 '필드 member' 데이터인 '해당 회원 정보'는, 이제 db에 작업 후에도 '사용자'에게 리턴되지 않는다!
                //즉, 포스트맨 통신으로 확인할 때, 아래 '회원 객체'는 통신 결과에 나타나지 않는다! 즉, 무시된다!
    private Member member; //'현재 게시글'이 '어떤 사용자에 의해 작성되었는가'를 나타내주는 필드

}
