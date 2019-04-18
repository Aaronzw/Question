package com.wenda.controller;

import com.wenda.model.ViewObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DynamicController {
    @RequestMapping(path={"/dynamic"},method ={RequestMethod.POST,RequestMethod.GET})
    public String browseHistory(Model model){
        ViewObject vo=new ViewObject();
        model.addAttribute("data",vo);
        return "dynamic";
    }
}
