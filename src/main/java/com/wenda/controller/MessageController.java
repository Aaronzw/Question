package com.wenda.controller;

import com.wenda.dao.MessageDao;
import com.wenda.model.*;
import com.wenda.service.MessageService;
import com.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationList(Model model, @RequestParam("conversationId") String converstaionId){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";
        int localUserId=hostHolder.getUser().getId();
        List<Message> messageList=messageService.getConversationDetail(converstaionId);
        List<ViewObject> messages=new ArrayList<ViewObject>();
        for(Message message :messageList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targrtId=(message.getFromId()==localUserId?message.getToId():message.getFromId());
            vo.set("user",userService.getUser(targrtId));
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
        return "letterDetail";
    }
}
