package com.wenda.Admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.QuestionService;
import com.wenda.service.ReportService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class DealReportController {
    private static final Logger logger= LoggerFactory.getLogger(DealReportController.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReportService reportService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path = {"/getReportedQuestionList"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestRportedQuestionList(@RequestParam("offset")int offset,
                                             @RequestParam("limit")int limit,
                                             @RequestParam("status")int status){
        HashMap result=new HashMap();
        PageHelper.startPage(offset,limit);
        List<Report> reportlist=new ArrayList<>();
        try {
            reportlist=reportService.getQuestionListByStatus(status);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        PageInfo<Report> pageInfo=new PageInfo<Report>(reportlist);
        List<Map> data=new ArrayList<>();
        for(Report report:reportlist){
            HashMap map=new HashMap();
            Question question=questionService.getById(report.getEntityId());
            map.put("question",question);
            map.put("report",report);
            data.add(map);
        }
        result.put("data",data);
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(path = {"/getReportedAnswerList"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestRportedCommentList(@RequestParam("offset")int offset,
                                             @RequestParam("limit")int limit,
                                             @RequestParam("status")int status){
        HashMap result=new HashMap();
        PageHelper.startPage(offset,limit);
        List<Report> reportlist=new ArrayList<>();
        try {
            reportlist=reportService.getAnswerListByStatus(status);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        PageInfo<Report> pageInfo=new PageInfo<Report>(reportlist);
        List<Map> data=new ArrayList<>();
        for(Report report:reportlist){
            HashMap map=new HashMap();
            Comment answer=commentService.getCommentById(report.getEntityId());
            map.put("answer",answer);
            map.put("report",report);
            data.add(map);
        }
        result.put("data",data);
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(path = {"/getReportedUserList"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestRportedUserList(@RequestParam("offset")int offset,
                                             @RequestParam("limit")int limit,
                                             @RequestParam("status")int status){
        HashMap result=new HashMap();
        PageHelper.startPage(offset,limit);
        List<Report> reportlist=new ArrayList<>();
        try {
            reportlist=reportService.getUserListByStatus(status);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        PageInfo<Report> pageInfo=new PageInfo<Report>(reportlist);
        List<Map> data=new ArrayList<>();
        for(Report report:reportlist){
            HashMap map=new HashMap();
            User user=userService.getUser(report.getEntityId());
            map.put("user",user);
            map.put("report",report);
            data.add(map);
        }
        result.put("data",data);
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(path = {"/handleDealtReport"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String handleDealt(@RequestParam("reportId")int reportId,
                                             @RequestParam("resultCode")int resultCode){
        if(hostHolder.getUser()==null)
            return WendaUtil.getJSONString(999);
        boolean ret=false;
        ret=reportService.dealReport(reportId,hostHolder.getUser().getId(),resultCode,new Date());
        if(ret){
            return WendaUtil.getJSONString(0,"success");
        }else {
            return WendaUtil.getJSONString(1,"fail");
        }
    }
}
