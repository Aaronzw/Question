package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.QuestionService;
import com.wenda.service.RecommenderService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class RecommenderController {
    private static Logger logger= LoggerFactory.getLogger(RecommenderController.class);

    @Autowired
    RecommenderService recommenderService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @RequestMapping(path = {"/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String recMemder(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){

        int userId;
        Date date=new Date();
        if(hostHolder.getUser()!=null)
            userId=hostHolder.getUser().getId();
        else
            userId=1;
//        List<ViewObject> vos=getRecomenderList(userId,10);
//        model.addAttribute("rec_ques",vos);
        List<Integer> questionIds=recommenderService.getRecomenderItemsForUser(userId);

        model.addAttribute("rec_totals",questionIds.size());
        Date date1=new Date();
        double s=(date1.getTime()-date.getTime())/1000.0;
        return "index";
    }
//    @RequestMapping(path = {"/"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String index(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
//
//
//        return "index";
//    }
    public List<ViewObject> getRecomenderList(int userId,int num){
        List<Integer> questionIds=recommenderService.getRecomenderItemsForUser(userId);
        List<ViewObject> vos=new ArrayList<>();
        int cnt=0;
//        for(Integer qid:questionIds){
//            ViewObject vo=new ViewObject();
//            HashMap questionMap=new HashMap();
//            Question question=questionService.getById(qid);
//            User user=userService.getUser(question.getUserId());
//            questionMap.put("question",question);
//            questionMap.put("user",user);
//            vo.set("questionMap",questionMap);
//            vos.add(vo);
//            cnt++;
//            if(cnt>=num)
//                break;
//        }
        return vos;
    }

    @RequestMapping(path ={"/request/recomQuestionsForUser"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String recTwoPartMethod(@RequestParam("limit")int limit,
                                   @RequestParam("offset")int offset){
        HashMap result=new HashMap();
        HashMap questionMap=new HashMap();
        int userId;
        if(hostHolder.getUser()==null)
            return WendaUtil.getJSONString(999);
        userId=hostHolder.getUser().getId();
        List<Integer> questionIds=recommenderService.getRecomenderItemsForUser(userId);
        //若数据太少无法产生推荐结果则随机推荐
        WendaUtil.pageStart(offset,limit);
        ArrayList<Integer> new_questionIdList=WendaUtil.pageHelper(questionIds);
        List<Map> list=new ArrayList<>();
        for(Integer qid:new_questionIdList){
            questionMap=new HashMap();
            Question question=questionService.getById(qid);
            User user=userService.getUser(question.getUserId());
            questionMap.put("question",question);
            questionMap.put("user",user);
            list.add(questionMap);
        }
        result.put("data",list);
        result.put("code",0);
        result.put("count",questionIds.size());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

    //针对特定的问题请求推荐结果
    @RequestMapping(path ={"/reuestt/getRecomenderQuestion"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String getRecmenderQuestion(@RequestParam("questionId")int questionId){
        HashMap result=new HashMap();
        HashMap questionMap=new HashMap();

        List<Integer> questionIds=recommenderService.getRecomenderItemsForItem(questionId);
        PageInfo<Integer> pageInfo=new PageInfo<>(questionIds);
        List<Map> list=new ArrayList<>();
        int RecomenderListLimit=3;//最多推荐3个
        for(int i=0;i<RecomenderListLimit&&i<questionIds.size();i++){
            questionMap=new HashMap();
            Question question=questionService.getById(questionIds.get(i));
            User user=userService.getUser(question.getUserId());
            questionMap.put("question",question);
            questionMap.put("user",user);
            list.add(questionMap);
        }
        result.put("data",list);
        result.put("code",0);
//        result.put("has_next",pageInfo.isHasNextPage());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
}
