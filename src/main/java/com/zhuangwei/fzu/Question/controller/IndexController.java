package com.zhuangwei.fzu.Question.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @RequestMapping(path = {"/index","/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        int a=2;
        int b=2*a;
        return "index";
    }
}
