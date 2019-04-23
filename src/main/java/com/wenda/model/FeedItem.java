package com.wenda.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class FeedItem implements Comparable<FeedItem> {
    private int entityType;
    private int entityId;
    private String data;
    private JSONObject dataJson;
    private Date createdDate;
    private double sortNum;

    public double getSortNum() {
        return sortNum;
    }

    public void setSortNum(double sortNum) {
        this.sortNum = sortNum;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJson=JSON.parseObject(data);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    /*重载排序，默认降序*/
    public int compareTo(FeedItem f) {
        // TODO Auto-generated method stub
        return (f.sortNum-this.sortNum)>0?1:-1;
    }
};
