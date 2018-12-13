package com.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String regloginPage(Model model) {
        return "reglogin";
    }

    @RequestMapping
}
