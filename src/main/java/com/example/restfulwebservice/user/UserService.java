package com.example.restfulwebservice.user;


import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
public class UserService {



    //임시로 '사용자 Users 들에 대한 리스트 객체'를 여기에 만듦. 관계형 DB 사용은 추후 강의에서 나옴.
    //이건 진짜 임시로만 사용하기 위해 아래처럼 만들어준 것임.
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3; //'최초 초기 사용자'는 3명으로 잡음(?)

    static{
        users.add(new User(1, "Kenneth", new Date()));
        users.add(new User(2, "Alice", new Date()));
        users.add(new User(3, "Elena", new Date()));
    }

    //< 전체 사용자 조회 >
    public List<User> findAll(){
        return users;
    }

    //< 사용자 추가 >
    public User save(User user){
        if(user.getId() == null){ //매개변수로 전달된 'user정보 내부'에 '기본키'에 해당하는 id가 존재하지 않는다면,
            user.setId(++usersCount); //여기서 우리가 id값을 하나씩 증가시키면서 해당 사용자에게 id값 부여해줌
        }
        users.add(user);
        return user;
    }


    //< 전체 사용자 조회 >
    public User findOne(int id){
        for(User user : users){ //int a : intArr  (자료형 변수명(for-each문 내부에서만 통용) : 배열명)
            if(user.getId()==id){ //if, 'User 객체 내부의 id 값' == '메소드 findOne의 매개변수로 들어온 id값' 이라면,
                return user; //해당 user 정보를 리턴해주고,
            }
        }
        return null; //같지 않다면, null을 리턴해줌.
    }

}
