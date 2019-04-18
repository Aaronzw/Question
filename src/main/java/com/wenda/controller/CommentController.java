package com.wenda.controller;

import com.wenda.model.*;
import com.wenda.service.*;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

@Controller
public class CommentController {
    private static final Logger logger=LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    ReadRecordService readRecordService;
    @Autowired
    FollowService followService;
    /*
    @param:questionId,content,userId
    针对问题发表回答的接口
    */
    @RequestMapping(value = {"/comment/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("entityId") int entityId,
                             @RequestParam("content") String content,
                             @RequestParam("entityType")int entityType
                            )
    {
        try{
            Comment comment=new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(entityId);
            comment.setEntityType(entityType);
            comment.setStatus(0);
            if(hostHolder.getUser()==null){
                return WendaUtil.getJSONString(-1,"anonymous");
            }else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            if(commentService.addComment(comment)>0)
            {
                //插入数据成功则返回插入成功的id
                int answer_id=comment.getId();
                //更新评论数
                int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
                questionService.updateCommentCount(comment.getEntityId(),count);
                Map map=new HashMap();
                map.put("answer_id",answer_id);
                map.put("msg","success");
                return WendaUtil.getJSONString(0,map);
            }
        }catch (Exception e){
            logger.error("发表评论失败");
        }
        return WendaUtil.getJSONString(1,"fail");
    }

    @RequestMapping(value = {"/answer/Detail"},method = {RequestMethod.GET})
    public String answerComment(Model model, @RequestParam("questionId") int questionId,
        @RequestParam("answerId") int answerId
                    ){
        Comment answer=commentService.getCommentById(answerId);
        Question question=questionService.getById(questionId);
        if(question==null||answer==null)
            return "404";
        if(answer.getEntityType()!=EntityType.ENTITY_QUESTION||answer.getEntityId()!=question.getId())
            return "404";
        Map questionMap=new HashMap();
        questionMap.put("browseCount",readRecordService.getBrowsedCount(EntityType.ENTITY_QUESTION,questionId));
        questionMap.put("question",question);
        questionMap.put("followStatus",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
        questionMap.put("followerCnt",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
        model.addAttribute("questionMap",questionMap);
        Map answerMap=new HashMap();
        answerMap.put("user",userService.getUser(answer.getUserId()));
        answerMap.put("answer",answer);
        answerMap.put("commentCount",commentService.getCommentCount(answer.getId(),EntityType.ENTITY_COMMENT));
        if(hostHolder.getUser()!=null){
            answerMap.put("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,answer.getId()));
        }else {
            answerMap.put("liked",0);
        }

        model.addAttribute("answerMap",answerMap);
        Map commentInfo=new HashMap();
        commentInfo.put("commentCount",commentService.getCommentCount(question.getId(),EntityType.ENTITY_QUESTION));
        model.addAttribute("commentInfo",commentInfo);

        return "answerDetail";
    }

}
