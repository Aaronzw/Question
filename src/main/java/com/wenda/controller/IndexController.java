package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.*;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class IndexController {
    private static Logger logger= LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;

/*利用分页插件*/
    @RequestMapping(value = "/index/requestLatestAnswers",method = RequestMethod.POST)
    @ResponseBody
    public String MoreLatestAnswers(@RequestParam("userId") int userId,
                               @RequestParam("offset")int offset,
                               @RequestParam("limit") int limit){
        PageHelper.startPage(offset,limit);
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Comment> commentList=new ArrayList<>();

        try {
            //数据库不分页地查数据
            commentList=commentService.getLatestAnswers(userId);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        for(Comment comment:pageInfo.getList()){
            map=new HashMap<>();
            Map<String,Object> commentMap=new HashMap<>();
            Map<String,Object> questionMap=new HashMap<>();

            commentMap.put("comment",comment);
            commentMap.put("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            if(hostHolder.getUser()!=null){
                commentMap.put("likeStatus",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }else {
                commentMap.put("likeStatus",0);
            }
            commentMap.put("user",userService.getUser(comment.getUserId()));
            List<Comment> commentsForAnswer=commentService.getCommentListByEntity(comment.getId(),EntityType.ENTITY_COMMENT);
            commentMap.put("commentCount",commentsForAnswer.size());
            map.put("commentMap",commentMap);
            questionMap.put("question",questionService.getById(comment.getEntityId()));
            map.put("questionMap",questionMap);

            list.add(map);
        }
        result.put("code",0);
        result.put("data",list);
        result.put("has_next",pageInfo.isHasNextPage());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    public List<ViewObject> getLatestAnswers(int userId, int offset, int limit){
        PageHelper.startPage(offset,limit);
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Comment> commentList=new ArrayList<>();
        try{
            commentList=commentService.getLatestAnswers(0);
        }catch (Exception e){
            logger.error("查询数据库异常");
        }
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        List<ViewObject> vos=new ArrayList<>();
        for(Comment comment :pageInfo.getList()){
            ViewObject vo=new ViewObject();
            Map<String,Object> commentMap=new HashMap<>();
            Map<String,Object> questionMap=new HashMap<>();
            commentMap.put("comment",comment);
            commentMap.put("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            if(hostHolder.getUser()!=null){
                commentMap.put("likeStatus",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }else {
                commentMap.put("likeStatus",0);
            }
            List<Comment> commentsForAnswer=commentService.getCommentListByEntity(comment.getId(),EntityType.ENTITY_COMMENT);
            commentMap.put("commentCount",commentsForAnswer.size());
//            commentMap.put("commentCount",0);
            commentMap.put("user",userService.getUser(comment.getUserId()));
            vo.set("commentMap",commentMap);
            questionMap.put("question",questionService.getById(comment.getEntityId()));
            vo.set("questionMap",questionMap);

            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(value = "/index/requestLatestQuestions",method = RequestMethod.POST)
    @ResponseBody
    public String MoreLatestQuestions(@RequestParam("userId") int userId,
                                    @RequestParam("offset")int offset,
                                    @RequestParam("limit") int limit){
        PageHelper.startPage(offset,limit);
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Question> questionList=new ArrayList<>();

        try {
            //数据库不分页地查数据
            questionList=questionService.getLatestQuestionsPageHelper(userId);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Question> pageInfo=new PageInfo<>(questionList);
        for(Question question:pageInfo.getList()){
            map=new HashMap<>();
            Map<String,Object> questionMap=new HashMap<>();
            questionMap.put("question",question);
            questionMap.put("user",userService.getUser(question.getUserId()));
            map.put("questionMap",questionMap);
            list.add(map);
        }
        result.put("code",0);
        result.put("data",list);
        result.put("has_next",pageInfo.isHasNextPage());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    public List<ViewObject> getLatestQuestions(int userId, int offset, int limit){
        PageHelper.startPage(offset,limit);
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Question> questionList=new ArrayList<>();
        try{
            questionList=questionService.getLatestQuestionsPageHelper(0);
        }catch (Exception e){
            logger.error("查询数据库异常"+e.getMessage());
        }
        PageInfo<Question> pageInfo=new PageInfo<>(questionList);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question :pageInfo.getList()){
            ViewObject vo=new ViewObject();
            Map<String,Object> questionMap=new HashMap<>();
            questionMap.put("user",userService.getUser(question.getUserId()));
            questionMap.put("question",question);
            questionMap.put("followCount",0);
            if(hostHolder.getUser()!=null){
                if(followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,question.getId()))
                    questionMap.put("isFollower",1);
                else
                    questionMap.put("isFollower",0);
            }else {
                questionMap.put("isFollower",0);
            }

            vo.set("questionMap",questionMap);
            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){

        return "index";
    }
    @RequestMapping(path = {"/latest"},method = {RequestMethod.POST,RequestMethod.GET})
    public String latest(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        model.addAttribute("vos_ans",getLatestAnswers(0,0,5));
        model.addAttribute("vos_ques",getLatestQuestions(0,0,5));
        return "latest";
    }
}
