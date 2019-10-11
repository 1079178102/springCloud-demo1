package com.xph.user.mapper;

import com.xph.user.pojo.User;
import com.xph.user.pojo.UserInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select("select a.id,a.username,b.name from users as a,usersinfo as b where a.id = b.id")
    public List<UserInfo> findAllUserInfo();

    @Select("select a.id,a.username,a.password,b.name from users as a,usersinfo as b where a.id=b.id and a.username = #{username}")
    public UserInfo findUserInfoByUsername(String username);

}