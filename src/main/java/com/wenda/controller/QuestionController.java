package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import com.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class QuestionController {
    private static Logger logger= LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,@RequestParam("content") String content){
        try {
            Question question=new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setStatus(0);
            question.setCreatedDate(new Date());

            if(hostHolder.getUser()!=null){
                User user=hostHolder.getUser();
                question.setUserId(user.getId());
            }else {
                //通常不会有未登录用户，因为提问按钮登录用户才显示
                return WendaUtil.getJSONString(1,"annoymous");
            }
            if(questionService.addQuestion(question)>0){
                int question_id=question.getId();
                Map info=new HashMap();
                info.put("msg","success");
                info.put("question_id",question_id);
                return WendaUtil.getJSONString(0,info);
            }
        }catch (Exception e){
            logger.error("增加题目失败！"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"fail");
    }

    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);
        List<Comment> commentList=commentService.getCommentListByEntity(qid,EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<ViewObject>();
        for(Comment comment :commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            //未登录
            if(hostHolder.getUser()==null){
                vo.set("liked",0);
            }else {
                //暂定
                vo.set("liked",1);
            }
            //likeservice暂定
            vo.set("likeCount",9);
//            vo.set("comments_count,);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        int commentCount=commentList.size();
        model.addAttribute("commentCount",commentCount);
        return "question_detail";
    }
    @RequestMapping(value = "/question/requestMore",method = RequestMethod.POST)
    @ResponseBody
    public String MoreQuestion1(@RequestParam("questionId") int questionId,
                                @RequestParam("offset")int offset,
                                @RequestParam("limit") int limit){
        //使用开源pageHelper插件
        PageHelper.startPage(offset,limit);
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> map=new HashMap();
        List<Map> list=new ArrayList<>();
        List<Comment> commentList=new ArrayList<>();
        //数据库不分页地查数据
        try {
            commentList=commentService.getCommentListByEntity(questionId,EntityType.ENTITY_QUESTION);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        for(Comment comment:pageInfo.getList()){
            map=new HashMap<>();
            map.put("comment",comment);
            if(hostHolder.getUser()==null){
                map.put("liked",0);
            }else {
                //暂定
                map.put("liked",1);
            }
            //likeservice暂定
            map.put("likeCount",9);
//            vo.set("comments_count,);
            User user=userService.getUser(comment.getUserId());
            //避免密码暴露于ajax
            user.setPassword("*************");
            user.setSalt("***********");
            map.put("user",user);
            list.add(map);
        }
        result.put("code",0);
        result.put("data",list);
        result.put("has_next",pageInfo.isHasNextPage());
        result.put("current_pages",pageInfo.getPageNum());
        result.put("totals",pageInfo.getTotal());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }

}
