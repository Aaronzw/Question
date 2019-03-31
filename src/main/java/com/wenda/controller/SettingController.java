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
public class SettingController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @RequestMapping(path={"/setting"},method ={RequestMethod.POST,RequestMethod.GET})
    public String test(Model model){
        ViewObject vo=new ViewObject();
        model.addAttribute("data",vo);
        return "user/profile";
    }
}
