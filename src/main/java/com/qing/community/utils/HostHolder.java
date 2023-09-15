package com.qing.community.utils;

import com.qing.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用Threadlocal容器存储用户信息，保证线程安全
 */
@Component
public class HostHolder {
    ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
