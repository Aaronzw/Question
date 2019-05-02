package com.wenda.Admin.service;

import com.sun.org.apache.regexp.internal.RE;
import com.wenda.model.HostHolder;
import com.wenda.model.PrivageLevel;
import com.wenda.model.User;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrivageService {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    public HashMap updateUserPri(int userId,int pri){
        HashMap result=new HashMap();
        User user=userService.getUser(userId);
        if(user==null) {
            result.put("code",1);
            result.put("msg","该用户不存在");
            return result;
        }
        if(hostHolder.getUser()==null){
            result.put("code",1);
            result.put("msg","未登录无法操作");
            return result;
        }
        if(privageAdmin(hostHolder.getUser().getId())){
            int res=userService.upUserPri(userId,pri);
            if(res>0){
                result.put("code",0);
                result.put("msg","success");
            }else {
                result.put("code",1);
                result.put("msg","fail");
            }
        }else {
            result.put("code",1);
            result.put("msg","当前用户无操作权限");

        }
        return result;
    }
    public boolean privageAdmin(int userId){
        User user=userService.getUser(userId);
        if(user==null)
            return false;
        if(user.getPriLv()!=PrivageLevel.pri_user){
            return true;
        }else {
            return false;
        }
    }
    public boolean privageRoot(int userId){
        User user=userService.getUser(userId);
        if(user==null)
            return false;
        if(user.getPriLv()==PrivageLevel.pri_root){
            return true;
        }else {
            return false;
        }
    }

    //获取不同权限人群列表
    public List<User> getUserPriType(int pri_lv){
        List<User> userList=userService.getUserPriType(pri_lv);
        return userList;
    }
}
