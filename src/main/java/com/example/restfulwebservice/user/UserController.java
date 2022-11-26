package com.example.restfulwebservice.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService){ //생성자를 통한 의존성주입!
        this.userService = userService;
    }



    @PostMapping("/users")
    public  void createUser(@RequestBody User user){ //@RequestBody : 사용자가 작성하여 보낸 'JSON 객체에 담겨진 내용'이 넘어올 때
                                                     //               이를 받기 위해 여기에 컨트롤러에 작성함.
                                                     //               전달받고자 하는 이 데이터가 'RequestBody 형식'의 역할을
                                                     //               하겠다고 선언하는 것.
        User savedUser = userService.save(user);

    }
}
