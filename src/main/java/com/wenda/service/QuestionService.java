package com.wenda.service;

import com.wenda.dao.QuestionDao;
import com.wenda.model.Question;
import com.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    public Question getById(int id){
        return questionDao.getById(id);
    }
    public int addQuestion(Question question){
        //过滤html标签
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question)>0?question.getId():0;
    }

    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }
    public List<Question> getLatestQuestionsPageHelper(int userId){
        return questionDao.selectLatestQuestionsPageHelper(userId);
    }
    public int updateCommentCount(int id,int count){
        return questionDao.updateCommentCount(id,count);
    }
    public  List<Integer> getQuestionIdList(){
        List<Integer> idlist=new ArrayList<>();
        List<Question> questionList=questionDao.getQuestionList();
        for(Question question:questionList){
            idlist.add(question.getId());
        }
        return idlist;
    }
}
