package com.wenda.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.model.*;
import com.wenda.service.*;
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
    @Autowired
    LikeService likeService;
    @Autowired
    ReadRecordService readRecordService;
    @Autowired
    FollowService followService;
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
        if(question.getStatus()==Constant.Question_deleted&&(hostHolder==null||hostHolder.getUser().getPriLv()==PrivageLevel.pri_user))
            return  "404";
        Map questionMap=new HashMap();
        Map followerInfo=new HashMap();
        if(question!=null&&hostHolder.getUser()!=null){
            readRecordService.userBrowseAdd(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,qid);
        }
        User user=userService.getUser(question.getUserId());
        questionMap.put("user",user);
        questionMap.put("question",question);
        if(hostHolder.getUser()==null){
            questionMap.put("followStatus",0);
        }else {
            questionMap.put("followStatus",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,qid)?1:0);
        }
        model.addAttribute("questionMap",questionMap);

        followerInfo.put("followerCnt",followService.getFollowerCount(EntityType.ENTITY_QUESTION,qid));
        List<User> followerList=userService.UserIdsToUserList(followService.getFollowers(EntityType.ENTITY_QUESTION,qid,11));
        followerInfo.put("followerList",followerList);
        followerInfo.put("browseCnt",readRecordService.getBrowsedCount(EntityType.ENTITY_QUESTION,qid));
        model.addAttribute("followInfo",followerInfo);
        List<Comment> commentList=commentService.getCommentListByEntity(qid,EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<ViewObject>();
        for(Comment comment :commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            //未登录
            if(hostHolder.getUser()==null){
                vo.set("liked",0);
            }else {
                int localUserId=hostHolder.getUser().getId();
                vo.set("liked",likeService.getLikeStatus(localUserId,EntityType.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        int commentCount=commentList.size();
        model.addAttribute("commentCount",commentCount);
        return "question_detail";
    }

    /*entityType.question=1,comment=2*/
    @RequestMapping(value = "/question/requestMore",method = RequestMethod.POST)
    @ResponseBody
    public String MoreQuestion1(@RequestParam("entityId") int entityId,
                                @RequestParam("entityType") int entityType,
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
            commentList=commentService.getCommentListByEntity(entityId,entityType);
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
                map.put("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }
            map.put("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            map.put("commentCount",commentService.getCommentCount(comment.getId(),EntityType.ENTITY_COMMENT));
            User user=userService.getUser(comment.getUserId());
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

    @RequestMapping(path ={"/deleteQuestion"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String followUserId(@RequestParam("questionId")int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }
        int localUserId=hostHolder.getUser().getId();
        if(localUserId!=question.getUserId()&&hostHolder.getUser().getPriLv()==PrivageLevel.pri_user){
            return WendaUtil.getJSONString(1,"当前用户无删除权限");
        }
        try {
            int ret=questionService.deleteQuestion(questionId);
            return ret>0?WendaUtil.getJSONString(0,String.valueOf(ret)):WendaUtil.getJSONString(1,"fail");
        }catch (Exception e){
            return WendaUtil.getJSONString(1,e.getMessage());
        }
        //成功则返回code=0，msg=粉丝数；失败则返回code=999，下同
//        return WendaUtil.getJSONString(1,"fail");
    }

}
