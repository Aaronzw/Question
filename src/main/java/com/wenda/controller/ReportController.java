package com.wenda.controller;

import com.wenda.model.*;
import com.wenda.service.*;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ReportController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    //举报问题ajax
    @RequestMapping(path ={"/reportQuestion"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String reportQuestion(@RequestParam("questionId")int questionId,@RequestParam("reason") String reason){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }
        int localUserId=hostHolder.getUser().getId();

        Report report=new Report();
        report.setUseId(localUserId);
        report.setReason(reason);
        report.setCreatedDate(new Date());
        report.setEntity(EntityType.ENTITY_QUESTION,questionId);
        try {
            int ret=reportService.addReport(report);
            return WendaUtil.getJSONString(0,String.valueOf(ret));
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
//        return WendaUtil.getJSONString(1,"fail");
    }
    //举报用户ajax
    @RequestMapping(path ={"/reportUser"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String reportUser(@RequestParam("userId")int userId,@RequestParam("reason") String reason){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        User user=userService.getUser(userId);
        if(user==null){
            return WendaUtil.getJSONString(1,"用户不存在");
        }
        int localUserId=hostHolder.getUser().getId();

        Report report=new Report();
        report.setUseId(localUserId);
        report.setReason(reason);
        report.setCreatedDate(new Date());
        report.setEntity(EntityType.ENTITY_USER,userId);
        try {
            int ret=reportService.addReport(report);
            return WendaUtil.getJSONString(0,String.valueOf(ret));
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
//        return WendaUtil.getJSONString(1,"fail");
    }

    //举报用户ajax
    @RequestMapping(path ={"/reportAnswer"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String reportAnswer(@RequestParam("answerId")int answerId,@RequestParam("reason") String reason){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment=commentService.getCommentById(answerId);
        if(comment==null){
            return WendaUtil.getJSONString(1,"回答不存在");
        }
        int localUserId=hostHolder.getUser().getId();

        Report report=new Report();
        report.setUseId(localUserId);
        report.setReason(reason);
        report.setCreatedDate(new Date());
        report.setEntity(EntityType.ENTITY_COMMENT,answerId);
        try {
            int ret=reportService.addReport(report);
            return WendaUtil.getJSONString(0,String.valueOf(ret));
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
//        return WendaUtil.getJSONString(1,"fail");
    }
}
