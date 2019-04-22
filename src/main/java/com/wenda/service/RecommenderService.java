package com.wenda.service;

import com.wenda.model.EntityType;
import com.wenda.model.SortItem;
import com.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommenderService {

    @Autowired
    FollowService followService;
    @Autowired
    ReadRecordService readRecordService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;


    public List<Integer> getRecomenderItemsForUser(int userId){

        List<Integer> userlist=userService.getUserIdList();

        List<Integer> questionIdList=questionService.getQuestionIdList();

        HashMap<Integer,HashMap<Integer,Integer>> usermatrix=getMatrixBasedOnUsers(userlist);

        HashMap<Integer,HashMap<Integer,Integer>> questionmatrix=getMatrixBasedOnItems(questionIdList);

        List<Integer> recomenderList=getRecListOnUserIdByTwoPartMethod(userId,userlist,questionIdList,usermatrix,questionmatrix);
        return recomenderList;

    }

    public List<Integer> getRecomenderItemsForItem(int itemId){
        List<Integer> list=new ArrayList<>();
        return list;

    }
    public List<Integer> getRecomenderUsersForUser(int itemId){
        List<Integer> list=new ArrayList<>();
        return list;
    }
    //用户-问题二维map，第一维是user
    public HashMap<Integer,HashMap<Integer,Integer>> getMatrixBasedOnUsers(List<Integer> userIds){
        HashMap<Integer,HashMap<Integer,Integer>> matrx=new HashMap<>();
        for(Integer id:userIds){
            List<Integer> qidList=readRecordService.getAllBrowseList(id,EntityType.ENTITY_QUESTION);
            HashMap<Integer,Integer> map=new HashMap<>();
            for(Integer qid:qidList){
                map.put(qid,1);
            }
            matrx.put(id,map);
        }
        return matrx;
    }
    //用户-问题二维map，第一维是QUESTION
    public HashMap<Integer,HashMap<Integer,Integer>> getMatrixBasedOnItems(List<Integer> questionIds){
        HashMap<Integer,HashMap<Integer,Integer>> matrx=new HashMap<>();
        double sum=0;
        for(Integer qid:questionIds){
            Date date=new Date();
            List<Integer> userIdList=readRecordService.getAllBrowsedList(EntityType.ENTITY_QUESTION,qid);
            Date date1=new Date();
            HashMap<Integer,Integer> map=new HashMap<>();
            for(Integer uid:userIdList){
                map.put(uid,1);
            }
            matrx.put(qid,map);
            sum=sum+(date1.getTime()-date.getTime())/1000.0;
        }
        System.out.println(sum);
        return matrx;
    }


    //基于资源流动的二部图
    public List<Integer> getRecListOnUserIdByTwoPartMethod(int userId,
                                                           List<Integer>userIdLists,
                                                           List<Integer> itemIdList,
                                                           HashMap<Integer,HashMap<Integer,Integer>> userMatrix,
                                                           HashMap<Integer,HashMap<Integer,Integer>> itemMatrix){
        List<Integer> result=new ArrayList<>();
        HashMap<Integer,Double> rankUser=new HashMap();
        HashMap <Integer,Double> rankItem=new HashMap();
        rankUser.put(userId,1d);
        int times=2;
        for(int i=1;i<=times;i++){
            for(Integer user:userIdLists){
                if (rankUser.containsKey(user)){
                    HashMap<Integer,Integer> raltedItems=userMatrix.get(user);
                    for(Integer item:raltedItems.keySet()){
                        if(rankItem.containsKey(item)){
                            rankItem.put(item,rankItem.get(item)+rankUser.get(user)/raltedItems.size());
                        }else {
                            rankItem.put(item,rankUser.get(user)/raltedItems.size());
                        }
                    }
                }
            }
            rankUser=new HashMap<Integer, Double>();
            for(Integer itemid:itemIdList){
                if (rankItem.containsKey(itemid)){
                    HashMap<Integer,Integer> raltedUsers=itemMatrix.get(itemid);
                    for(Integer user:raltedUsers.keySet()){
                        if(rankUser.containsKey(user)){
                            rankUser.put(user,rankUser.get(user)+rankItem.get(itemid)/raltedUsers.size());
                        }else {
                            rankUser.put(user,rankItem.get(itemid)/raltedUsers.size());
                            int a=1+1;
                        }

                    }
                }
            }
            if(i!=times)
                rankItem=new HashMap<Integer, Double>();
        }
        List<SortItem<Integer>> sortItems=new ArrayList<>();
        //对rank[item]进行排序
        for(Map.Entry<Integer,Double> entry:rankItem.entrySet()){
            SortItem<Integer> sortItem=new SortItem<>();
            sortItem.setItem(entry.getKey());
            sortItem.setSortNum(entry.getValue());
            sortItems.add(sortItem);
        }

        Collections.sort(sortItems);
        HashMap<Integer,Integer> cur_user_like=userMatrix.get(userId);
        //把当前userId喜欢过的item不加入推荐结果
        for(SortItem<Integer> item:sortItems){
            if(!cur_user_like.containsKey(item.getItem()))
                result.add(item.getItem());
        }
        return result;
    }
}
