package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.dao.MessageDao;
import com.wenda.model.*;
import com.wenda.service.MessageService;
import com.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MessageController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @RequestMapping(value = "/msg/list", method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
        List<Message> messageList=messageService.getConversationList(localUserId);
        List<ViewObject> conversations=new ArrayList<ViewObject>();
        for(Message message :messageList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targrtId=(message.getFromId()==localUserId?message.getToId():message.getFromId());
            vo.set("user",userService.getUser(targrtId));
            vo.set("unReadCount",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }
    //ajax请求接口，conversationList
    @RequestMapping(value = "/msg/list/request", method = {RequestMethod.POST})
    @ResponseBody
    public String getMsgList(Model model,
                             @RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("userId")int userId){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
        if(localUserId!=userId)
            return "redirect:/reglogin";

        PageHelper.startPage(offset,limit);
        Map result=new HashMap();
        Map data=new HashMap();
        List<Map> itemList=new ArrayList<>();
        List<Message> messageList=new ArrayList<>();
        try {
            messageList=messageService.getConversationList(localUserId);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Message> pageInfo=new PageInfo<>(messageList);
        List<ViewObject> conversations=new ArrayList<ViewObject>();
        for(Message message :messageList){
            Map item=new HashMap();
            item.put("message",message);
            int targrtId=(message.getFromId()==localUserId?message.getToId():message.getFromId());
            item.put("user",userService.getUser(targrtId));
            item.put("unReadCount",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
            itemList.add(item);
        }
        result.put("data",itemList);
        result.put("code",0);
        result.put("totals",pageInfo.getTotal());
        result.put("has_next",pageInfo.isHasNextPage());
        result.put("current_pages",pageInfo.getPageNum());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String converstaionId){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
        List<Message> messageList=messageService.getConversationDetail(converstaionId);
        List<ViewObject> messages=new ArrayList<ViewObject>();
        User targetUser=new User();
        for(Message message :messageList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targrtId=(message.getFromId()==localUserId?message.getToId():message.getFromId());
            vo.set("user",userService.getUser(targrtId));
            targetUser=userService.getUser(targrtId);
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
        model.addAttribute("conversationId",converstaionId);
        model.addAttribute("targetUser",targetUser);
        return "letterDetail";
    }
    //ajax请求接口，conversationDetail
    @RequestMapping(value = "/msg/detail/request", method = {RequestMethod.POST})
    @ResponseBody
    public String getMsgDetail(Model model,
                             @RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("conversationId")String conversationId){
//        if(hostHolder.getUser()==null)
////            return "redirect:/reglogin";
////        int localUserId=hostHolder.getUser().getId();
        int localUserId=11;
        if(!conversationId.startsWith(""+localUserId)&&!conversationId.endsWith(""+localUserId))
            return "redirect:/reglogin";
        PageHelper.startPage(offset,limit);
        Map result=new HashMap();
        Map data=new HashMap();
        List<Map> itemList=new ArrayList<>();
        List<Message> messageList=new ArrayList<>();
        try {
            messageList=messageService.getConversationDetail(conversationId);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Message> pageInfo=new PageInfo<>(messageList);
        List<ViewObject> conversations=new ArrayList<ViewObject>();
//        String conversationId=new String();
        for(Message message :messageList){
            Map item=new HashMap();
            item.put("message",message);
            int targrtId=(message.getFromId()==localUserId?message.getToId():message.getFromId());
            item.put("user",userService.getUser(targrtId));
            itemList.add(item);
        }
        result.put("conversationId",conversationId);
        result.put("data",itemList);
        result.put("code",0);
        result.put("totals",pageInfo.getTotal());
        result.put("has_next",pageInfo.isHasNextPage());
        result.put("current_pages",pageInfo.getPageNum());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(value = "/msg/add", method = {RequestMethod.POST})
    @ResponseBody
    public String msgAdd(Model model,
                               @RequestParam("content")String content,
                               @RequestParam("fromId")int fromId,
                               @RequestParam("toId")int toId){
        Map result=new HashMap();
        Message message=new Message();
        message.setContent(content);
        message.setToId(toId);
        message.setFromId(fromId);
        message.setCreatedDate(new Date());
        message.setConversationId(message.getConversationId());
        try {
            int mid=messageService.add(message);
        }catch (Exception e){
            result.put("code",0);
            result.put("msg","插入数据失败"+e);
        }
        result.put("code",0);
        result.put("msg","success");
        return JSON.toJSONString(result);
    }
}
