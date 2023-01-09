package com.example.restfulwebservice.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
                            //- '레펏 JpaRepository'는 '자신이 지정하고자(다루고자) 하는 엔티티'에 대해
                            //'그것이 어떤 엔티티인지(=Member)'와 '그 엔티티의 기본키 id 타입(=Integer)'을 명시해야 함.
                            //이 작업만으로도 CRUD 가능하다!
                            //- ctrl누르고 'JpaRepository 버튼' 위에 마우스 올려서 그 속성 들어가보면
                            //  '메소드 findAll' 등 다양하게 이미 내장되어 있음을 확인 가능하다!
                            //- 그리고, '레펏 JpaRepository'가 상속받고 있는
                            //  '레펏 PagingAndSortingRepository'와 그것이 또 상속받고 있는
                            //  '레펏 CrudRepository', 또 그것이 상속받고 있는
                            //  '레펏 Repository' 속의 '내장 메소드들'을 확인할 수 있다!
                            //- 즉, '레펏 MemberRepository'를 사용함으로써, 상속의 상속의 상속이 과정으로
                            //  결과적으로, '레펏 JpaRepository', '레펏 PagingAndSortingRepository',
                            //  '레펏 Repository' 속의 '모든 내장 메소드들'을 사용할 수 있게 된다!!




}
