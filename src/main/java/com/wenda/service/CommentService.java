package com.wenda.service;

import com.wenda.dao.CommentDao;
import com.wenda.model.Comment;
import com.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;
    //发表评论
    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }
    //获取评论列表
    public List<Comment> getCommentListByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }
    //删除评论 状态置为1，表示删除
    public boolean deleteComment(int id){
        return commentDao.updateStatus(id,1)>0;
    }
    //查询问题的评论数和评论的评论数
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }
    //根据id获得回答
    public Comment getCommentById(int commentId){
        return commentDao.getCommentById(commentId);
    }

    public int getUserCommentCount(int userId){
        return commentDao.getUserCommentCount(userId);
    }

    public List<Comment> getLatestAnswers(int userId){
        return commentDao.getLatestAnswers(userId);
    }
}
