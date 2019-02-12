package com.wenda.controller;

import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static Logger logger= LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/question/add",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,@RequestParam("content") String content){
        try {
            Question question=new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setStatus(0);
            question.setCreatedDate(new Date());

            if(hostHolder.getUser()!=null){
                User user=hostHolder.getUser();
                question.setUserId(user.getId());
            }else {
                //通常不会有未登录用户，因为提问按钮登录用户才显示
                return WendaUtil.getJSONString(1,"annoymous");
            }
            if(questionService.addQuestion(question)>0){
                return WendaUtil.getJSONString(0,"success");
            }
        }catch (Exception e){
            logger.error("增加题目失败！"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"fail");
    }

    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question=questionService.getById(qid);

        return "question_detail";
    }

}
