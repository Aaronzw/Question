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
    public void deleteComment(int entityId,int entityType){
        commentDao.updateStatus(entityId,entityType,1);
    }
    //查询问题的评论数和评论的评论数
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }
}
