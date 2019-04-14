package com.wenda.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);

    @RequestMapping(path = {"/admin/login"},method = {RequestMethod.POST,RequestMethod.GET})
    public String adMinLogin(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        return "admin/login";
    }
    @RequestMapping(path = {"/admin"},method = {RequestMethod.POST,RequestMethod.GET})
    public String admminIndex(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        return "admin/admin";
    }
}
