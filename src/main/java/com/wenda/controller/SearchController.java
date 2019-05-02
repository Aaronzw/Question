package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.FollowService;
import com.wenda.service.QuestionService;
import com.wenda.service.SearchService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("keyWord") String keyword
                         ) {
        int qTotals=0,uTotals=0;
        List<HashMap> uList=new ArrayList<>();
        try {
            qTotals=searchService.SearchtQuestionTotals(keyword);
            uTotals=searchService.SearchtUserTotals(keyword);
        } catch (Exception e) {
            logger.error("搜索评论失败" + e.getMessage());
        }
        model.addAttribute("questionTotals",qTotals);
        model.addAttribute("userTotals",uTotals);
        model.addAttribute("keyWord",keyword);
        return "result";
    }
    @RequestMapping(value = "/search/user/request", method = {RequestMethod.POST})
    @ResponseBody
    public String getUserList(Model model,
                             @RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("keyWord")String keyWord){
        Map result=new HashMap();
        List<HashMap> data=new ArrayList<>();
        try {
            data=searchService.SearchtUserInfo(keyWord,offset,limit);
            PageInfo<HashMap> pageInfo=new PageInfo<>(data);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }

        result.put("code",0);
        result.put("data",data);

        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(value = "/search/question/request", method = {RequestMethod.POST})
    @ResponseBody
    public String getQuestionList(Model model,
                             @RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("keyWord")String keyWord){
        Map result=new HashMap();
        List<HashMap> data=new ArrayList<>();
        try {
            data=searchService.SearchtQuestionInfo(keyWord,offset,limit);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        result.put("code",0);
        result.put("data",data);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
}
