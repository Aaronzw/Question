package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.FollowService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import com.wenda.util.JedisAdapter;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*关注的人和粉丝列表*/
    @RequestMapping(path = {"/follow/{userId}"},method = {RequestMethod.POST,RequestMethod.GET})
    public String latest(Model model ,@PathVariable("userId")int userId){
        model.addAttribute("cur_user",userService.getUser(userId));
        return "user/follow";
    }

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
    public String followQuestion(@RequestParam("questionId")int questionId){
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
    public String unFollowQuestion(@RequestParam("questionId")int questionId){
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


    @RequestMapping(value = "/request/followerList", method = {RequestMethod.POST})
    @ResponseBody
    /*offset 从1开始的位移量，页数；
    *limit 每页大小；
    *userId 求对应user的follower
    */
    public String getFollowerList(@RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("userId")int userId){
        Map result=new HashMap();
        long count=0;
        List<Integer> followerList=new ArrayList<>();
        List<Map> list=new ArrayList<>();
        try {
            count=followService.getFollowerCount(EntityType.ENTITY_USER,userId);
            /*redis底层offset从0开始*/
            offset=offset-1;
            followerList=followService.getFollowers(EntityType.ENTITY_USER,userId,offset*limit,limit);
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        for(Integer i:followerList){
            Map map=new HashMap();
            User user=userService.getUser(i);
            if(user!=null)
            {
                map.put("user",user);
                double timeScore=followService.getFollowerTime(userId,user.getId());
                /*fasttime转date转string*/
                String time=WendaUtil.DateFormat(WendaUtil.longFastTime2Date((long)timeScore));
                map.put("followTime",time);
                if(hostHolder.getUser()==null)
                    map.put("followStatus",0);
                else
                    map.put("followStatus",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,user.getId())?1:0);
                list.add(map);
            }
        }
        Map data=new HashMap();
        data.put("totals",count);
        data.put("list",list);
        result.put("code",0);
        result.put("data",data);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(value = "/request/followeeList", method = {RequestMethod.POST})
    @ResponseBody
    /*offset 从1开始的位移量，页数；
     *limit 每页大小；
     *userId 求对应user的follower
     */
    public String getFolloweeList(@RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("userId")int userId){

        Map result=new HashMap();
        long count=0;
        List<Integer> followeeList=new ArrayList<>();
        List<Map> list=new ArrayList<>();
        try {
            count=followService.getFolloweeCount(userId,EntityType.ENTITY_USER);
            /*redis底层offset从0开始*/
            offset=offset-1;
            followeeList=followService.getFollowees(userId,EntityType.ENTITY_USER,offset*limit,limit);
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        for(Integer i:followeeList){
            Map map=new HashMap();
            User user=userService.getUser(i);
            if(user!=null)
            {
                map.put("user",user);
                double timeScore=followService.getFolloweeTime(userId,EntityType.ENTITY_USER,user.getId());
                /*fasttime转date转string*/
                String time=WendaUtil.DateFormat(WendaUtil.longFastTime2Date((long)timeScore));
                map.put("followTime",time);
                if(hostHolder.getUser()==null)
                    map.put("followStatus",0);
                else
                    map.put("followStatus",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,user.getId())?1:0);
                list.add(map);
            }

        }
        Map data=new HashMap();
        data.put("totals",count);
        data.put("list",list);
        result.put("code",0);
        result.put("data",data);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
}
