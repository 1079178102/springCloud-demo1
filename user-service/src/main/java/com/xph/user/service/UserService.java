package com.xph.user.service;

import com.xph.user.mapper.UserMapper;
import com.xph.user.pojo.User;
import com.xph.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(Long id) {
        // 模拟超时
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userMapper.selectByPrimaryKey(id);
    }

    public List<UserInfo> findAllUserInfo(){
        return  userMapper.findAllUserInfo();
    }

    public UserInfo findUserInfoByUsername(String username){
        return userMapper.findUserInfoByUsername(username);
    }
}
