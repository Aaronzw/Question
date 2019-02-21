package com.wenda.service;

import com.wenda.util.JedisAdapter;
import com.wenda.util.RedisKeyUtil;
import org.apache.velocity.runtime.directive.Parse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;
    //userId关注某一类实体 entityType entityId
    public  boolean follow(int userId,int entityType,int entityId){
        //目标实体的粉丝集合加一
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        //userId关注的某一类实体集合
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        //返回执行成功的指令的数量，2
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        //两条都执行成功才成功
        return ret.size()==2&&(long)ret.get(0)>0&&(long)ret.get(1)>0;
    }
    //userId取关某一类实体 entityType entityId
    public  boolean unfollow(int userId,int entityType,int entityId){
        //目标实体的粉丝集合加一
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        //userId关注的某一类实体集合
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));
        //返回执行成功的指令的数量，2
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        //两条都执行成功才成功
        return ret.size()==2&&(long)ret.get(0)>0&&(long)ret.get(1)>0;
    }
    //获取粉丝集合（时间有序集）的前count个元素
    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }
    //偏移offsert个，取offset到offset+count之间的元素
    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,offset,offset+count));
    }
    //获取关注集合（时间有序集）的前count个元素
    public List<Integer> getFollowees(int userId,int entityType,int count){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey,0,count));
    }
    //偏移offsert个，取offset到offset+count之间的元素
    public List<Integer> getFollowees(int userId,int entityType,int offset,int count){
        String followeeKey=RedisKeyUtil.getFollowerKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey,offset,offset+count));
    }
    //获取粉丝数量
    public long getFollowerCount(int entityType,int entityId){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }

    //获取关注的一类实体数量
    public long getFolloweeCount(int UserId,int entityType){
        String followeeKey=RedisKeyUtil.getFolloweeKey(UserId,entityType);
        return jedisAdapter.zcard(followeeKey);
    }
    //判断user是否关注该实体
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }

    public List<Integer> getIdsFromSet(Set<String> set){
        List<Integer> idList=new ArrayList<>();
        for(String item:set){
            idList.add(Integer.parseInt(item));
        }
        return idList;
    }

}
