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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value = "/question/{qid}",method = RequestMethod.POST)
    @ResponseBody
    public String questionDetail(Model model, @RequestParam("qid") int qid){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);
        List<Comment> commentList=commentService.getCommentListByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos=new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();
            User user=new User();
            vo.set("user",user);
            vo.set("comment",comment);
            vos.add(vo);
        }
        model.addAttribute("comments",vos);
        return "detail";
    }

}
