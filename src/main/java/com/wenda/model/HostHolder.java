package com.wenda.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    /*
    * 用于多线程登录功能，选择当前用户账号
    */
    private static ThreadLocal<User> users=new ThreadLocal<>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
