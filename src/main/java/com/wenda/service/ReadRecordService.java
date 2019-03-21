package com.wenda.service;

import com.wenda.util.JedisAdapter;
import com.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;

/*
* 用户浏览记录
* 问题被浏览记录
* 问题热度统计
* */

@Service
public class ReadRecordService {
    @Autowired
    JedisAdapter jedisAdapter;

    //userId关注某一类实体 entityType entityId
    public  boolean follow(int userId,int entityType,int entityId){

        //目标实体的粉丝集合加一
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);

        //userId关注的某一类实体集合
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);

        /*事务操作*/
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));

        //返回执行成功的指令的数量，2
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        //两条都执行成功才成功
        return ret.size()==2&&(long)ret.get(0)>0&&(long)ret.get(1)>0;
    }
    //userId取关某一类实体 entityType entityId
    public  boolean unfollow(int userId,int entityType,int entityId){

        //目标实体的粉丝集合减一
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

}
