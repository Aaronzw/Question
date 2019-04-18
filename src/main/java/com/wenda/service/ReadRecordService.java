package com.wenda.service;

import com.sun.org.apache.regexp.internal.RE;
import com.wenda.model.EntityType;
import com.wenda.model.Question;
import com.wenda.model.SortItem;
import com.wenda.util.JedisAdapter;
import com.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;

/*
* 用户浏览记录
* 问题被浏览记录
* 问题热度统计
* */

@Service
public class ReadRecordService {
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    QuestionService questionService;
    //userId浏览过某一类实体 entityType entityId
    public  boolean userBrowseAdd(int userId,int entityType,int entityId){

        //目标实体的浏览者集合加一
        String browseRecordKey= RedisKeyUtil.getBrowseRecordKey(userId,entityType);
        //userId浏览的某一类实体集合
        String browsedRecordKey=RedisKeyUtil.getBrowsedRecordKey(entityType,entityId);

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
        String browseRecordKey= RedisKeyUtil.getBrowseRecordKey(userId,entityType);
        //userId浏览的某一类实体集合
        String browsedRecord=RedisKeyUtil.getBrowsedRecordKey(entityType,entityId);

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
        String browseRecordkey=RedisKeyUtil.getBrowseRecordKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(browseRecordkey,offset,offset+count));
    }

    /*获取访问者列表*/
    public List<Integer> getBrowsedRecordList(int entityType,int entityId,int offset,int count){
        String browsedRecordkey=RedisKeyUtil.getBrowsedRecordKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(browsedRecordkey,offset,offset+count));
    }
    /*获取被访问记录次数*/
    public long getBrowsedCount(int entityType,int entityId) {
        String browsedRecordkey=RedisKeyUtil.getBrowsedRecordKey(entityType,entityId);
        return jedisAdapter.zcard(browsedRecordkey);
    }
    /**获取访问记录次数**/
    public long getBrowseCount(int userId,int entityType) {
        String browsedRecordkey=RedisKeyUtil.getBrowseRecordKey(userId,entityType);
        return jedisAdapter.zcard(browsedRecordkey);
    }

    /*判断userId是否浏览过entitType,entityId*/
    public boolean AssertHasBrowse(int userId,int entityType,int entityId){
        String browseRecordkey=RedisKeyUtil.getBrowseRecordKey(userId,entityType);
        return jedisAdapter.zscore(browseRecordkey, String.valueOf(entityId)) != null;
    }

    public List<Integer> getIdsFromSet(Set<String> set){
        List<Integer> idList=new ArrayList<>();
        for(String item:set){
            idList.add(Integer.parseInt(item));
        }
        return idList;
    }
    public Double getBrowserTime(int userId,int entityId,int entityType){
        String browserKey=RedisKeyUtil.getBrowseRecordKey(userId,entityType);
        return jedisAdapter.zscore(browserKey,String.valueOf(entityId));
    }

    public List<Question> getHotQuestionDesc(){
        List<Question> questionList=questionService.getLatestQuestionsPageHelper(0);
        List<SortItem<Question>> sortItems=new ArrayList<>();
        for(Question question :questionList){
            long num=getBrowsedCount(EntityType.ENTITY_QUESTION,question.getId());
            SortItem<Question> sortItem=new SortItem<>();
            sortItem.setItem(question);
            sortItem.setSortItem((int)num);
            sortItem.setSortSecond(question.getCreatedDate().getTime());
            sortItems.add(sortItem);
        }
        Collections.sort(sortItems);
        List<Question> result=new ArrayList<>();
        for(SortItem<Question> sortItem:sortItems){
            result.add(sortItem.getItem());
        }
        return result;
    }
}
