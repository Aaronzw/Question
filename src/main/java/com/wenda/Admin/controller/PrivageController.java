package com.wenda.Admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.Admin.service.PrivageService;
import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.QuestionService;
import com.wenda.service.ReportService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class PrivageController {
    private static final Logger logger= LoggerFactory.getLogger(PrivageController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    PrivageService privageService;
    @Autowired
    ReportService reportService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @RequestMapping(path = {"/upPrivage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String upUserAdmin(@RequestParam("userId")int userId){
        HashMap result=privageService.updateUserPri(userId,PrivageLevel.pri_admin);
        String msg=result.get("msg").toString();
        return JSON.toJSONString(result);
    }
    @RequestMapping(path = {"/downPrivage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String downUserAdmin(@RequestParam("userId")int userId){
        HashMap result=privageService.updateUserPri(userId,PrivageLevel.pri_user);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(path = {"/getAdminsList"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestAdmins(@RequestParam("offset")int offset,@RequestParam("limit")int limit){
        HashMap result=new HashMap();
        PageHelper.startPage(offset,limit);
        List<User> userlist=new ArrayList<>();
        try {
             userlist=userService.getUserPriType(PrivageLevel.pri_admin);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        PageInfo<User> pageInfo=new PageInfo<User>(userlist);
        result.put("data",pageInfo.getList());
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(path = {"/getRootsList"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestRoots(@RequestParam("offset")int offset,@RequestParam("limit")int limit){
        HashMap result=new HashMap();
        PageHelper.startPage(offset,limit);
        List<User> userlist=new ArrayList<>();
        try {
            userlist=userService.getUserPriType(PrivageLevel.pri_root);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        PageInfo<User> pageInfo=new PageInfo<User>(userlist);
        result.put("data",pageInfo.getList());
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

    /*该接口请求全部用户数据，包括管理员和非管理员*/
    @RequestMapping(path = {"/getUsersList"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestUsers(@RequestParam("offset")int offset,@RequestParam("limit")int limit,int status){
        HashMap result=new HashMap();
        PageHelper.startPage(offset,limit);
        List<User> userlist=new ArrayList<>();
        try {
            userlist=userService.getAllUsers(status);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        PageInfo<User> pageInfo=new PageInfo<User>(userlist);
        result.put("data",pageInfo.getList());
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(path = {"/requestStatisticsDatas"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestStatisticsDatas(){
        HashMap result=new HashMap<>();
        List<User> userList=userService.getUserPriType(PrivageLevel.pri_user);
        List<User> adminList=userService.getUserPriType(PrivageLevel.pri_admin);
        List<User> rootList=userService.getUserPriType(PrivageLevel.pri_root);
        List<Question> questionList=questionService.getLatestQuestionsPageHelper(0);
        List<Comment> answerList=commentService.getEntityTypeList(EntityType.ENTITY_QUESTION);
        List<Comment> commentList=commentService.getEntityTypeList(EntityType.ENTITY_COMMENT);
        result.put("code",0);
        result.put("userCnt",userList.size());
        result.put("adminCnt",adminList.size());
        result.put("rootCnt",rootList.size());
        result.put("questionCnt",questionList.size());
        result.put("commentCnt",commentList.size());
        result.put("answerCnt",answerList.size());

        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    /*封禁用户*/
    @RequestMapping(path = {"/admin/banUser"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestBanUser(@RequestParam("userId")int userId){
        HashMap result=new HashMap<>();
        if(hostHolder.getUser()==null)
        {
            return WendaUtil.getJSONString(999);
        }
        User user=userService.getUser(userId);
        if(user==null){
            return WendaUtil.getJSONString(1,"该用户不存在");
        }
        int ret=userService.updateUserStatus(userId,Constant.User_banned);
        if(ret>0){
            return WendaUtil.getJSONString(0,"success");
        }else {
            return WendaUtil.getJSONString(1,"fail");
        }
    }
    /*解禁用户*/
    @RequestMapping(path = {"/admin/rebanUser"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String requestReBanUser(@RequestParam("userId")int userId){
        HashMap result=new HashMap<>();
        if(hostHolder.getUser()==null)
        {
            return WendaUtil.getJSONString(999);
        }
        User user=userService.getUser(userId);
        if(user==null){
            return WendaUtil.getJSONString(1,"该用户不存在");
        }
        int ret=userService.updateUserStatus(userId,Constant.User_normal);
        if(ret>0){
            return WendaUtil.getJSONString(0,"success");
        }else {
            return WendaUtil.getJSONString(1,"fail");
        }
    }
}
