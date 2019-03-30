package com.wenda.controller;

import com.wenda.model.HostHolder;
import com.wenda.model.User;
import com.wenda.model.ViewObject;
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
    @RequestMapping(path={"/user/{userId}"},method ={RequestMethod.POST,RequestMethod.GET})
    public String test(Model model, @PathVariable("userId")int userId){
        User user=userService.getUser(userId);
        ViewObject vo=new ViewObject();
        if(hostHolder.getUser()!=null&&hostHolder.getUser().getId()==user.getId()){
            vo.set("myProfile","1");
        }else {
            vo.set("myProfile","0");
        }
        vo.set("user",user);
        model.addAttribute("data",vo);
        return "user/profile";
    }
}
