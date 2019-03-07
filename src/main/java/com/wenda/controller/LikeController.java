package com.wenda.controller;

import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.service.CommentService;
import com.wenda.service.LikeService;
import com.wenda.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

    //点赞ajax链接
    @RequestMapping(path ={"/comment/like"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String likeComment(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        //int userId=1;
        Comment comment=commentService.getCommentById(commentId);
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.like(userId,EntityType.ENTITY_COMMENT,commentId);
        //成功则返回code=0，msg=集合元素数；失败则返回code=999，下同
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    /*对评论点踩，ajax*/
    @RequestMapping(path ={"/comment/dislike"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String disLikeComment(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment=commentService.getCommentById(commentId);
        //int userId=1;
        int userId=hostHolder.getUser().getId();
        long disLikeCount=likeService.disLike(userId,EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(disLikeCount));
    }
}
