package com.wenda.controller;

import com.wenda.model.User;
import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.model.Question;
import com.wenda.service.FollowService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import com.wenda.util.JedisAdapter;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    //关注用户ajax链接
    @RequestMapping(path ={"/followUser"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String followUserId(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        User user=userService.getUser(userId);
        if(user==null){
            return WendaUtil.getJSONString(1,"用户不存在");
        }
        int localUserId=hostHolder.getUser().getId();
        Boolean ret=followService.follow(localUserId,EntityType.ENTITY_USER,userId);

        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
        return WendaUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(userId,EntityType.ENTITY_USER)));
    }

    @RequestMapping(path ={"/unFollowUser"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String unFollowUserId(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        User user=userService.getUser(userId);
        if(user==null){
            return WendaUtil.getJSONString(1,"用户不存在");
        }
        int localUserId=hostHolder.getUser().getId();
        Boolean ret=followService.unfollow(localUserId,EntityType.ENTITY_USER,userId);
        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
        return WendaUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(userId,EntityType.ENTITY_USER)));
    }

    @RequestMapping(path ={"/followQuestion"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String followQuestion(@RequestParam("question")int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }
        int localUserId=hostHolder.getUser().getId();
        Boolean ret=followService.follow(localUserId,EntityType.ENTITY_QUESTION,questionId);

        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
        return WendaUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(questionId,EntityType.ENTITY_QUESTION)));
    }

    @RequestMapping(path ={"/unFollowQuestion"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String unFollowQuestion(@RequestParam("question")int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }
        int localUserId=hostHolder.getUser().getId();
        Boolean ret=followService.unfollow(localUserId,EntityType.ENTITY_QUESTION,questionId);

        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
        return WendaUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(questionId,EntityType.ENTITY_QUESTION)));
    }
}
