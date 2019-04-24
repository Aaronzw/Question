package com.wenda.controller;

import com.wenda.model.*;
import com.wenda.service.MessageService;
import com.wenda.service.QuestionService;
import com.wenda.service.ReportService;
import com.wenda.service.UserService;
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

    //举报问题ajax
    @RequestMapping(path ={"/reportQuestion"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String followUserId(@RequestParam("questionId")int questionId,@RequestParam("reason") String reason){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"用户不存在");
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


}
