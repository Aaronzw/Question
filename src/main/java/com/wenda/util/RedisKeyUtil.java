package com.wenda.util;

import com.wenda.model.EntityType;

public class RedisKeyUtil {
    private static String SPLIT="_";

    //redis key前缀
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_FOLLOWER="FOLLOWER";//被关注的
    private static String BIZ_FOLLOWEE="FOLLOWEE";//关注别人的人

//    private static String BIZ_HASREAD="HASREAD";//浏览记录信息
//    private static String BIZ_BEENHASREAD="BEENREAD";//被用户浏览，用户集合key

    private static String BIZ_TIMELINE="TIMELINE";//时间线推送

    //user浏览过的实体
    private static String BIZ_BROWSE_RECORD="BROWSE_RECORD";
    //浏览过该实体的人
    private static String BIZ_BROWSED_RECORD="BROWSED_RECORD";

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


    //浏览某一类实体记录集合，一般是问题记录
    public static String getBrowseRecord(int userId, int EntityType){
        return BIZ_BROWSE_RECORD+SPLIT+String.valueOf(userId)+SPLIT+String.valueOf(EntityType);
    }
    //浏览过某一实体的用户集合
    public static String getBrowsedRecord( int EntityType,int EntityId){
        return BIZ_BROWSED_RECORD+SPLIT+String.valueOf(EntityType)+SPLIT+String.valueOf(EntityId);
    }

}
