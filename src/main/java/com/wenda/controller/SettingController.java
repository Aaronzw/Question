package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.wenda.model.HostHolder;
import com.wenda.model.User;
import com.wenda.model.ViewObject;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        return "setting";
    }
    @RequestMapping(path={"/updatePassword"},method ={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String test(@RequestParam("nowpass")String nowpass,@RequestParam("pass")String pass){
        Map result=new HashMap();
        if(hostHolder.getUser()==null)
            return WendaUtil.getJSONString(999);
        int ret=userService.updatePassword(hostHolder.getUser().getId(),pass);
        if(ret>0){
            return WendaUtil.getJSONString(0,"success");
        }else {
            return WendaUtil.getJSONString(1,"fail");
        }
    }

}
