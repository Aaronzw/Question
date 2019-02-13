package com.wenda.controller;

import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.service.CommentService;
import com.wenda.service.LikeService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,@RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);
//            if(hostHolder.getUser()==null){
//                return "redirect:/reglogin";
//            }else {
//                comment.setUserId(hostHolder.getUser().getId());
//            }
            comment.setUserId(1);
            commentService.addComment(comment);

            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getId(),count);
        }catch (Exception e){
            logger.error("发表评论失败");
        }
        return "redirect:/question/"+questionId;
    }

}
