package com.wenda.service;

import com.wenda.util.JedisAdapter;
import com.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/*
* 用户浏览记录
* 问题被浏览记录
* 问题热度统计
* */

@Service
public class ReadRecordService {
    @Autowired
    JedisAdapter jedisAdapter;

    //userId浏览过某一类实体 entityType entityId
    public  boolean userBrowseAdd(int userId,int entityType,int entityId){

        //目标实体的粉丝集合加一
        String browseRecordKey= RedisKeyUtil.getBrowseRecord(userId,entityType);
        //userId浏览的某一类实体集合
        String browsedRecordKey=RedisKeyUtil.getBrowsedRecord(entityType,entityId);

        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        /*事务操作*/
        tx.zadd(browseRecordKey,date.getTime(),String.valueOf(entityId));
        tx.zadd(browsedRecordKey,date.getTime(),String.valueOf(userId));

        //返回执行成功的指令的数量，2
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        //两条都执行成功才成功
        return ret.size()==2&&(long)ret.get(0)>0&&(long)ret.get(1)>0;
    }

    //userId取关某一类实体 entityType entityId
    public  boolean userBrowseRemove(int userId,int entityType,int entityId){

        //目标实体的粉丝集合加一
        String browseRecordKey= RedisKeyUtil.getBrowseRecord(userId,entityType);
        //userId浏览的某一类实体集合
        String browsedRecord=RedisKeyUtil.getBrowsedRecord(entityType,entityId);

        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        tx.zrem(browseRecordKey,String.valueOf(userId));
        tx.zrem(browsedRecord,String.valueOf(entityId));
        //返回执行成功的指令的数量，2
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        //两条都执行成功才成功
        return ret.size()==2&&(long)ret.get(0)>0&&(long)ret.get(1)>0;
    }

    //获取浏览记录列表
    public List<Integer> getBrowseRecordList(int userId,int entityType,int offset,int count){
        String browseRecordkey=RedisKeyUtil.getBrowseRecord(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(browseRecordkey,offset,offset+count));
    }
    /*获取访问者列表*/
    public List<Integer> getBrowsedRecordList(int entityType,int entityId,int offset,int count){
        String browsedRecordkey=RedisKeyUtil.getBrowsedRecord(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(browsedRecordkey,offset,offset+count));
    }
    /*判断userId是否浏览过entitType,entityId*/
    public boolean AssertHasBrowse(int userId,int entityType,int entityId){
        String browseRecordkey=RedisKeyUtil.getBrowseRecord(userId,entityType);
        return jedisAdapter.zscore(browseRecordkey, String.valueOf(entityId)) != null;
    }

    public List<Integer> getIdsFromSet(Set<String> set){
        List<Integer> idList=new ArrayList<>();
        for(String item:set){
            idList.add(Integer.parseInt(item));
        }
        return idList;
    }
}
