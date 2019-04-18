package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.wenda.model.*;
import com.wenda.service.QuestionService;
import com.wenda.service.ReadRecordService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SettingController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    ReadRecordService readRecordService;
    @Autowired
    QuestionService questionService;
    @RequestMapping(path={"/setting"},method ={RequestMethod.POST,RequestMethod.GET})
    public String setting(Model model){
        ViewObject vo=new ViewObject();
        model.addAttribute("data",vo);
        return "setting";
    }
    @RequestMapping(path={"/history"},method ={RequestMethod.POST,RequestMethod.GET})
    public String browseHistory(Model model){
        if(hostHolder.getUser()==null){
            return "redirect:/login";
        }
        ViewObject vo=new ViewObject();
        model.addAttribute("data",vo);
        long totals=readRecordService.getBrowseCount(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION);
        model.addAttribute("browseTotals",totals);
        return "browseHistory";
    }

    /*offset当前页，从1开始*/
    @RequestMapping(path={"/request/questionHistory"},method ={RequestMethod.POST})
    @ResponseBody
    public String requestHistory(@RequestParam("limit")int limit,
                                 @RequestParam("offset")int offset,
                                 @RequestParam(value = "userId",required = false,defaultValue = "-1") int userId){
        if(hostHolder.getUser()==null&&userId==-1){
            return WendaUtil.getJSONString(1,"请登录");
        }
        if(userId==-1){
            userId=hostHolder.getUser().getId();
        }
        Map result=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Integer> questionIdList=new ArrayList<>();
        /*redis底层从0开始 */
        offset--;
        try {
            questionIdList=readRecordService.getBrowseRecordList(userId,EntityType.ENTITY_QUESTION,offset*limit,limit);
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        for (Integer i:questionIdList){
            Map map=new HashMap();
            Question question=questionService.getById(i);
            User user=userService.getUser(question.getUserId());
            map.put("user",user);
            map.put("question",question);

            double timeScore=readRecordService.getBrowserTime(userId,question.getId(),EntityType.ENTITY_QUESTION);
            /*fasttime转date*/
            Date time=WendaUtil.longFastTime2Date((long)timeScore);
            map.put("browserTime",time);
            list.add(map);
        }

        result.put("code",0);
        result.put("data",list);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(path={"/followQuestion/list"},method ={RequestMethod.POST,RequestMethod.GET})
    public String followQuesList(Model model){
        ViewObject vo=new ViewObject();
        model.addAttribute("data",vo);
        return "follow_question_list";
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
