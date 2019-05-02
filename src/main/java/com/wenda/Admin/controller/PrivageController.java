package com.wenda.Admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.Admin.service.PrivageService;
import com.wenda.model.HostHolder;
import com.wenda.model.PrivageLevel;
import com.wenda.model.User;
import com.wenda.service.QuestionService;
import com.wenda.service.ReportService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
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

    @RequestMapping(path = {"/upPrivage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String upUserAdmin(@RequestParam("userId")int userId){
        HashMap result=privageService.updateUserPri(userId,PrivageLevel.pri_admin);
        String msg=result.get("code").toString();
        return WendaUtil.getJSONString(0,msg);
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
        result.put("totals",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }


}
