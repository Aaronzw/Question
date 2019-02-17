package com.wenda.model;

import java.lang.annotation.ElementType;
import java.util.Date;

public class Message {
    private int id;
    private String content;
    private int fromId;
    private int toId;
    private String conversationId;
    private Date createdDate;
    private int hasRead;//默认0，0未读，1已读

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        if(fromId<toId){
            return this.fromId+"_"+this.toId;
        }else{
            return this.toId+"_"+this.fromId;
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }



}
