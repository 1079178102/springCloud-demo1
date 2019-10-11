package com.xph.user.controller;

import com.xph.user.pojo.User;
import com.xph.user.pojo.UserInfo;
import com.xph.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public User findById(@PathVariable("id")Long id){return userService.findById(id);}

    @GetMapping("/findAllUserInfo")
    public List<UserInfo> findAllUserInfo(){
        return  userService.findAllUserInfo();
    }

    @GetMapping("/findUserInfoByUsername/{str}/{username}")
    public UserInfo findUserInfoByUsername(
            @PathVariable("str")String str,
            @PathVariable("username")String username){
        System.out.println("* " + str);
        System.out.println("** " + username);
        return  userService.findUserInfoByUsername(username);
    }
}
