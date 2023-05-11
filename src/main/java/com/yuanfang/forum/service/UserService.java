package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.UserMapper;
import com.yuanfang.forum.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserById(int id){
        return userMapper.getUserById(id);
    }

}
