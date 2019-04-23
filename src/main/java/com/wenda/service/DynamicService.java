package com.wenda.service;

import com.wenda.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DynamicService {
    @Autowired
    FollowService followService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    public List<FeedItem> getFolloweeDynamic(int userId){
        List<FeedItem> feedItems=new ArrayList<>();
        int count=(int)followService.getFolloweeCount(userId,EntityType.ENTITY_USER);
        List<Integer> userList=followService.getFollowees(userId,EntityType.ENTITY_USER,count);
        List<Question> questionList=getQuestionDynamic(userList);
        List<Comment> commentList=getCommentDynamic(userList);
        //获取排序后的动态id和type，可能是回答，可能是提问
        for(Question question:questionList){
            FeedItem feedItem=new FeedItem();
            feedItem.setEntityType(EntityType.ENTITY_QUESTION);
            feedItem.setEntityId(question.getId());
            feedItem.setSortNum(question.getCreatedDate().getTime());
            feedItem.setCreatedDate(question.getCreatedDate());
            feedItems.add(feedItem);
        }
        for(Comment comment:commentList){
            FeedItem feedItem=new FeedItem();
            feedItem.setEntityType(EntityType.ENTITY_COMMENT);
            feedItem.setEntityId(comment.getId());
            feedItem.setSortNum(comment.getCreatedDate().getTime());
            feedItem.setCreatedDate(comment.getCreatedDate());
            feedItems.add(feedItem);
        }
        Collections.sort(feedItems);
        return feedItems;
    }
    public List<Question> getQuestionDynamic(List<Integer> userIds){
        List<Question> questionList=new ArrayList<>();
        for(Integer id:userIds){
            List<Question> userQuestionList=questionService.getLatestQuestionsPageHelper(id);
            questionList.addAll(userQuestionList);
        }
        return questionList;
    }
    public List<Comment> getCommentDynamic(List<Integer> userIds){
        List<Comment> commentList=new ArrayList<>();
        for(Integer id:userIds){
            List<Comment> userCommentList=commentService.getLatestAnswers(id);
            commentList.addAll(userCommentList);
        }
        return commentList;
    }
}
