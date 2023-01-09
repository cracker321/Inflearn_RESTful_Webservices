package com.example.restfulwebservice.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
                            //- '레펏 JpaRepository'는 '자신이 지정하고자(다루고자) 하는 엔티티'에 대해
                            //'그것이 어떤 엔티티인지(=Post)'와 '그 엔티티의 기본키 id 타입(=Integer)'을 명시해야 함.
                            //이 작업만으로도 CRUD 가능하다!
}
