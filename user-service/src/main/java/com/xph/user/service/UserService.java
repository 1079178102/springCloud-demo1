package com.xph.user.service;

import com.xph.user.mapper.UserMapper;
import com.xph.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(Long id) {return userMapper.selectByPrimaryKey(id);}
}
