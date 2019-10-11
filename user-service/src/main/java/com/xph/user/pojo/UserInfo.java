package com.xph.user.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class UserInfo {

    private Long id;
    private String username;
    private String password;
    private String name;

}
