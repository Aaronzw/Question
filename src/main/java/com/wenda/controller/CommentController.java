package com.wenda.controller;

import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.LikeService;
import com.wenda.service.QuestionService;
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

import java.lang.reflect.Method;
import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger=LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @RequestMapping(value = {"/comment/add"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content,
                             @RequestParam("userId")int userId ){
        try{
            Comment comment=new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);
            if(hostHolder.getUser()==null){
                return WendaUtil.getJSONString(-1,"anonymous");
            }else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            //comment.setUserId(1);
            if(commentService.addComment(comment)>0)
            {
                int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
                questionService.updateCommentCount(comment.getId(),count);
                return WendaUtil.getJSONString(0,"success");
            }
        }catch (Exception e){
            logger.error("发表评论失败");
        }
//        return "redirect:/question/"+questionId;
        return WendaUtil.getJSONString(1,"fail");
    }

}
