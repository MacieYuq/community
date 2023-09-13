package com.qing.community.service;

import com.qing.community.dao.UserMapper;
import com.qing.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findByUserById (int id){
        return userMapper.selectById(id);
    }
}
