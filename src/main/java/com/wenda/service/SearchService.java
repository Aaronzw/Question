package com.wenda.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.model.Question;
import com.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    ReadRecordService readRecordService;

    public List<HashMap> SearchtUserInfo(String key,int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<User> userList=userService.searchUserName(key);
        PageInfo<User> pageInfo=new PageInfo<>(userList);
        List<HashMap>result=new ArrayList<>();

        for(User user:userList){
            HashMap map=new HashMap();
            map.put("user",user);
            map.put("followeeCnt",followService.getFolloweeCount(user.getId(),EntityType.ENTITY_USER));
            map.put("followerCnt",followService.getFollowerCount(EntityType.ENTITY_USER,user.getId()));
            if(hostHolder.getUser()==null){
                map.put("followStatus",0);
            }else {
                map.put("followStatus",followService.isFollower(
                        hostHolder.getUser().getId(),EntityType.ENTITY_USER,user.getId()));
            }
            result.add(map);
        }

        return result;
    }
    public int SearchtUserTotals(String key){
        List<User> userList=userService.searchUserName(key);
        return userList.size();
    }
    public List<HashMap> SearchtQuestionInfo(String key,int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questionList=questionService.searchByName(key);
        PageInfo<Question> pageInfo=new PageInfo<>(questionList);
        List<HashMap>result=new ArrayList<>();
        for(Question question:pageInfo.getList()){
            HashMap map=new HashMap();
            map.put("question",question);
            map.put("user",userService.getUser(question.getUserId()));
            map.put("browseCnt",readRecordService.getBrowsedCount(EntityType.ENTITY_QUESTION,question.getId()));
            result.add(map);
        }
        return result;
    }
    public int SearchtQuestionTotals(String key){
        List<Question> questionList=questionService.searchByName(key);
        return questionList.size();
    }
}

