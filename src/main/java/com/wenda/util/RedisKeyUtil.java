package com.wenda.util;

public class RedisKeyUtil {
    private static String SPLIT="_";

    //redis key前缀
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_FOLLOWER="FOLLOWER";//被关注的
    private static String BIZ_FOLLOWEE="FOLLOWEE";//关注别人的人
    private static String BIZ_TIMELINE="TIMELINE";//时间线推送

    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    //某一实体的粉丝，问题，回答，用户
    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    //某一用户对某一类实体的关注
    public static String getFolloweeKey(int UserId,int entityType){
        return BIZ_FOLLOWEE+SPLIT+String.valueOf(UserId)+SPLIT+String.valueOf(entityType);
    }
    public static String getTimeLineKey(int userId){
        return BIZ_TIMELINE+SPLIT+String.valueOf(userId);
    }

}
