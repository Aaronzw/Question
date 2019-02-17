package com.wenda.service;

import com.wenda.dao.MessageDao;
import com.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    @Autowired
    SensitiveService sensitiveService;
    public List<Message> getConversationList(int userId){
        return messageDao.getConversationList(userId);
    }
    public List<Message> getConversationDetail(String conversationId){
        return messageDao.getConversationDetail(conversationId);
    }

    /*添加消息记录*/
    public int add(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message)>0?message.getId():0;
    }
    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }
}
