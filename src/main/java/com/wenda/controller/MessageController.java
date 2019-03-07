package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.dao.MessageDao;
import com.wenda.model.*;
import com.wenda.service.MessageService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
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

    /*跳到消息列表页面*/
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
    /*跳转到消息详情页面*/
    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String converstaionId){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
        List<Message> messageList=messageService.getConversationDetail(converstaionId);
        List<ViewObject> messages=new ArrayList<ViewObject>();
        for(Message message :messageList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targetId=(message.getFromId()!=localUserId?message.getFromId():message.getToId());
            vo.set("user",userService.getUser(message.getFromId()));
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
        model.addAttribute("conversationId",converstaionId);
        User targetUser=userService.getUser(WendaUtil.getChatTarget(converstaionId,localUserId));
        model.addAttribute("targetUser",targetUser);
        return "letterDetail";
    }

    //ajax请求接口，conversationDetail，分页
    @RequestMapping(value = "/msg/detail/request", method = {RequestMethod.POST})
    @ResponseBody
    public String getMsgDetail(Model model,
                             @RequestParam("limit")int limit,
                             @RequestParam("offset")int offset,
                             @RequestParam("conversationId")String conversationId){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
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
            if(message.getHasRead()==Constant.Msg_notRead&&message.getToId()==localUserId){
                messageService.setMsgHasRead(message.getId(),Constant.Msg_hasRead);
            }
            //int targrtId=(message.getFromId()==localUserId?message.getFromId():message.getFromId());
            item.put("user",userService.getUser(message.getFromId()));
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
    /*发消息 content，fromId，toId*/
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
        message.setHasRead(0);
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

    /*用于跳转到给某人发私信链接*/
    @RequestMapping(value = "/sendMsgTo/{sendToId}", method = {RequestMethod.GET})
    public String jumpToSendMsgTo(Model model, @PathVariable("sendToId") int sendToId){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
        Message newmsg=new Message();
        newmsg.setFromId(localUserId);
        newmsg.setToId(sendToId);
        String conversationId=newmsg.getConversationId();
        return "redirect:/msg/detail?conversationId="+conversationId;
        //return "letterDetail";
    }
}
