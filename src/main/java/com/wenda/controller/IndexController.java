package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

//    @RequestMapping(value = "/index/renderMore",method = RequestMethod.POST)
//    @ResponseBody
    public String MoreQuestion(@RequestParam("userId") int userId,
                                        @RequestParam("offset")int offset,
                                        @RequestParam("limit") int limit){
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Question> questionList=new ArrayList<>();
        try {
            questionList=questionService.getLatestQuestions(userId,offset,limit);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        for(Question question:questionList){
            map=new HashMap<>();
            map.put("question",question);
            map.put("user",userService.getUser(question.getUserId()));
            list.add(map);
        }
        result.put("code",0);
        result.put("data",list);
        return JSON.toJSONString(result);
    }
    @RequestMapping(value = "/index/renderMore",method = RequestMethod.POST)
    @ResponseBody
    public String MoreQuestion1(@RequestParam("userId") int userId,
                               @RequestParam("offset")int offset,
                               @RequestParam("limit") int limit){
        //使用开源pageHelper插件
        PageHelper.startPage(offset,limit);
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Question> questionList=new ArrayList<>();
        //数据库不分页地查数据
        try {
            questionList=questionService.getLatestQuestionsPageHelper(userId);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Question> pageInfo=new PageInfo<>(questionList);
        for(Question question:pageInfo.getList()){
            map=new HashMap<>();
            map.put("question",question);
            map.put("user",userService.getUser(question.getUserId()));
            list.add(map);
        }
        result.put("code",0);
        result.put("data",list);
        result.put("has_next",pageInfo.isHasNextPage());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
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
        model.addAttribute("vos",getQuestions(0,0,6));
        return "index";
    }

//    @RequestMapping(path={"/user/{userId}"},method ={RequestMethod.POST,RequestMethod.GET})
//    public String useIndex(Model model, @PathVariable("userId") int userId){
//        model.addAttribute("vos",getQuestions(userId,0,10));
//        return "index";
//    }
}
