package com.wenda.service;

import com.wenda.dao.QuestionDao;
import com.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question)>0?question.getId():0;
    }

    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }
    public int updateCommentCount(int id,int count){
        return questionDao.updateCommentCount(id,count);
    }
}
