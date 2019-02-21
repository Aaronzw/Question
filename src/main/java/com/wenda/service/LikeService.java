package com.wenda.service;

import com.wenda.util.JedisAdapter;
import com.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    //获取点赞次数
    //被点赞可以是问题或回答实体
    public long getLikeCount(int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }
    //喜欢 1；不喜欢 -1 ；无 0
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismenber(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return jedisAdapter.sismenber(disLikeKey,String.valueOf(userId))?-1:0;
    }

    public long like(int userId,int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        //为该实体点赞的用户集合加入一人
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        //点赞之后就取消踩
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }
    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        //点赞之后就取消踩
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        //为该实体点赞的用户集合加入一人
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(disLikeKey);
    }
}
