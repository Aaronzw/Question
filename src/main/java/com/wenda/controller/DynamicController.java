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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class DynamicController {
    private static final Logger logger=LoggerFactory.getLogger(DynamicController.class);

    @Autowired
    DynamicService dynamicService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @RequestMapping(path={"/dynamic"},method ={RequestMethod.POST,RequestMethod.GET})
    public String browseHistory(Model model){
        if(hostHolder.getUser()==null)
            return "redirect:/?next=dynamic";
        int userId=hostHolder.getUser().getId();
        List<ViewObject> vos=getUserFollowDynamic(userId);
        model.addAttribute("data",vos);
        model.addAttribute("totals",vos.size());
        return "dynamic";
    }
    private List<ViewObject> getUserFollowDynamic(int userId){
        Date date=new Date();

        List<ViewObject> vos=new ArrayList<>();
        List<FeedItem> feedItems=dynamicService.getFolloweeDynamic(userId);
        for(FeedItem item:feedItems){
            ViewObject vo=new ViewObject();
            User user=null;
            Question question=null;
            Comment comment=null;
            if(item.getEntityType()==EntityType.ENTITY_QUESTION){
                question=questionService.getById(item.getEntityId());
                user=userService.getUser(question.getUserId());
                HashMap questionMap=new HashMap();
                questionMap.put("question",question);
                questionMap.put("user",user);
                vo.set("questionMap",questionMap);
                vo.set("entityType",1);
                vos.add(vo);
            }else if(item.getEntityType()==EntityType.ENTITY_COMMENT){
                comment=commentService.getCommentById(item.getEntityId());
                user=userService.getUser(comment.getEntityId());
                HashMap commentMap=new HashMap();
                commentMap.put("comment",comment);
                commentMap.put("user",user);
                vo.set("entityType",2);
                vo.set("commentMap",commentMap);
                vos.add(vo);
            }
        }
        Date date1=new Date();
        return vos;
    }

    @RequestMapping(value = {"/dynamic/request"},method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("limit") int limit,
                             @RequestParam(value = "offset")int offset
    ) {
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(-1,"anonymous");
        }
        int userId=hostHolder.getUser().getId();
        HashMap result=new HashMap();
        List<FeedItem> feedItemList=new ArrayList<FeedItem>();
        List<HashMap> datalist=new ArrayList<>();
        try {
            //不基于mybatisde的列表貌似不能用pageHelper插件，遂自己写一个简单的
            feedItemList=dynamicService.getFolloweeDynamic(userId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return WendaUtil.getJSONString(1,"查询动态失败,"+e.getMessage());
        }
        WendaUtil.pageStart(offset,limit);
        ArrayList<FeedItem> pageList=WendaUtil.pageHelper(feedItemList);
        for(FeedItem item:pageList){
            HashMap data=new HashMap();
            User user=null;
            Question question=null;
            Comment comment=null;
            if(item.getEntityType()==EntityType.ENTITY_QUESTION){
                question=questionService.getById(item.getEntityId());
                user=userService.getUser(question.getUserId());
                HashMap questionMap=new HashMap();
                questionMap.put("question",question);
                questionMap.put("user",user);
                data.put("questionMap",questionMap);
                data.put("entityType",1);
                datalist.add(data);
            }else if(item.getEntityType()==EntityType.ENTITY_COMMENT){
                comment=commentService.getCommentById(item.getEntityId());
                user=userService.getUser(comment.getUserId());
                HashMap commentMap=new HashMap();
                commentMap.put("comment",comment);
                commentMap.put("user",user);
                commentMap.put("likeStatus",likeService.getLikeStatus(userId,EntityType.ENTITY_COMMENT,comment.getId()));
                commentMap.put("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
                data.put("entityType",2);
                data.put("commentMap",commentMap);
                HashMap questionMap=new HashMap();
                question=questionService.getById(comment.getEntityId());
                questionMap.put("question",question);
                data.put("questionMap",questionMap);
                datalist.add(data);
            }
        }
        result.put("code",0);
        result.put("data",datalist);
        result.put("totals",feedItemList.size());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
}
