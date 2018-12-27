package com.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @RequestMapping(path = {"/test"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model) {
        return "test";
    }
}
