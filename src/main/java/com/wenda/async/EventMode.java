package com.wenda.async;

import com.wenda.util.MailSender;

import java.util.HashMap;
import java.util.Map;

public class EventMode {
    private EventType type;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwnerId;

    private Map<String,String> exts=new HashMap<>();

    public EventMode(){}

    public EventMode(EventType type){
        this.type=type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventMode setExt(String key,String value) {
        this.exts.put(key,value);
        return this;
    }

    public EventType getEventType() {
        return type;
    }

    public EventMode setEventType(EventType eventType) {
        this.type = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventMode setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventMode setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventMode setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventMode setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    private EventMode setExts(Map<String,String> map){
        this.exts=map;
        return this;
    }
    private Map<String, String> getExts(){
        return this.exts;
    }

}
