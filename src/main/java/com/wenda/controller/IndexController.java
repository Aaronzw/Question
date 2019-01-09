package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.wenda.model.HostHolder;
import com.wenda.model.Question;
import com.wenda.model.User;
import com.wenda.model.ViewObject;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class IndexController {
    private static Logger logger= LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/index/renderMore",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("userId") int userId,
                                        @RequestParam("offset")int offset,
                                        @RequestParam("limit") int limit){
        List<Question> questionList=questionService.getLatestQuestions(userId,0,10);
        String s=new String();
        List<ViewObject> vos=new ArrayList<>();
        for(Question question:questionList){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
            s=JSON.toJSONString(vo);
        }
        int i=0;
        s=JSON.toJSONString(vos);
        return JSON.toJSONString(vos);
    }
    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<Question> questionList=questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question:questionList){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }

    @RequestMapping(path={"/user/{userId}"},method ={RequestMethod.POST,RequestMethod.GET})
    public String useIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
    }
}
