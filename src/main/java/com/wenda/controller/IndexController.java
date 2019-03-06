package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.LikeService;
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
public class IndexController {
    private static Logger logger= LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
//    /*利用分页插件*/
//    @RequestMapping(value = "/index/renderMore",method = RequestMethod.POST)
//    @ResponseBody
//    public String MoreQuestion1(@RequestParam("userId") int userId,
//                               @RequestParam("offset")int offset,
//                               @RequestParam("limit") int limit){
//        //使用开源pageHelper插件
//        PageHelper.startPage(offset,limit);
//        Map<String,Object> result=new HashMap<>();
//        Map<String,Object> map=new HashMap();
//        List<Map> list=new ArrayList<>();
//        List<Question> questionList=new ArrayList<>();
//        //数据库不分页地查数据
//        try {
//            questionList=questionService.getLatestQuestionsPageHelper(userId);
//        }catch (Exception e){
//            result.put("code",1);
//            result.put("msg","请求数据库异常！");
//            return JSON.toJSONString(result);
//        }
//        PageInfo<Question> pageInfo=new PageInfo<>(questionList);
//        for(Question question:pageInfo.getList()){
//            map=new HashMap<>();
//            map.put("question",question);
//            map.put("user",userService.getUser(question.getUserId()));
//            list.add(map);
//        }
//        result.put("code",0);
//        result.put("data",list);
//        result.put("has_next",pageInfo.isHasNextPage());
//        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
//    }

/*利用分页插件*/
    @RequestMapping(value = "/index/requestLatestAnswers",method = RequestMethod.POST)
    @ResponseBody
    public String MoreLatestAnswers(@RequestParam("userId") int userId,
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
            commentList=commentService.getLatestAnswers(userId);
        }catch (Exception e){
            result.put("code",1);
            result.put("msg","请求数据库异常！");
            return JSON.toJSONString(result);
        }
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        for(Comment comment:pageInfo.getList()){
            map=new HashMap<>();
            Map<String,Object> commentMap=new HashMap<>();
            Map<String,Object> questionMap=new HashMap<>();

            commentMap.put("comment",comment);
            commentMap.put("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            if(hostHolder.getUser()!=null){
                commentMap.put("likeStatus",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }else {
                commentMap.put("likeStatus",0);
            }
            map.put("commentMap",commentMap);
//            questionMap.put("followCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
//            if(hostHolder.getUser()!=null){
//                commentMap.put("likeStatus",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()))
//            }else {
//                commentMap.put("likeStatus",0);
//            }
            questionMap.put("question",questionService.getById(comment.getEntityId()));
            map.put("questionMap",questionMap);
            map.put("user",userService.getUser(comment.getUserId()));
            list.add(map);
        }
        result.put("code",0);
        result.put("data",list);
        result.put("has_next",pageInfo.isHasNextPage());
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
    }
    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<Question> questionList=questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question:questionList){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
//        model.addAttribute("vos",getQuestions(0,0,6));
        model.addAttribute("vos",commentService.getLatestAnswers(0));
        return "index";
    }

}
