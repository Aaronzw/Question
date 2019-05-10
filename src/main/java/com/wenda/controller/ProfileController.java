package com.wenda.controller;

import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.model.User;
import com.wenda.model.ViewObject;
import com.wenda.service.FollowService;
import com.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @RequestMapping(path={"/user/{userId}"},method ={RequestMethod.POST,RequestMethod.GET})
    public String test(Model model, @PathVariable("userId")int userId){
        User user=userService.getUser(userId);
        ViewObject vo=new ViewObject();
        if(hostHolder.getUser()==null||hostHolder.getUser().getId()!=user.getId()){
            vo.set("myProfile","0");
        }else {
            vo.set("myProfile","1");
        }

//        if(hostHolder.getUser()!=null&&hostHolder.getUser().getId()==user.getId()){
//            vo.set("myProfile","1");
//        }else {
//            vo.set("myProfile","0");
//        }
        if(hostHolder.getUser()!=null){
            vo.set("isFollower",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId)?1:0);
        }else {
            vo.set("isFollower",0);
        }
        vo.set("followerCnt",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("followeeCnt",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        vo.set("user",user);
        model.addAttribute("data",vo);
        return "user/profile";
    }
}
