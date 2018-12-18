package com.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    @RequestMapping(path = {"/index","/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        return "index";
    }
    @RequestMapping(path = {"/test"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model) {
        return "test";
    }
}
