package com.wenda;


import com.alibaba.fastjson.JSON;
import com.wenda.controller.IndexController;
import com.wenda.controller.QuestionController;
import com.wenda.dao.LoginTicketDao;
import com.wenda.dao.QuestionDao;
import com.wenda.model.LoginTicket;
import com.wenda.model.Question;
import com.wenda.model.User;
import com.wenda.dao.UserDao;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionApplication.class)
@WebAppConfiguration
public class QuestionApplicationTests {
	@Autowired
	UserDao userDao;
	@Autowired
	LoginTicketDao loginTicketDao;

	@Autowired
	UserService userService;
	@Autowired
	QuestionService questionService;

	@Autowired
	QuestionDao questionDao;

	@Autowired
	QuestionController questionController;
	@Autowired
	IndexController indexController;
	//插入假数据
	@Test
	public void contextLoads() {
		Random random=new Random();
		for(int i=0;i<10;i++){
			User user=new User();
			user.setName("user"+i);
			user.setHeadUrl("http://images.nowcoder.com/head/"+random.nextInt(1000)+"t.png");
			user.setSalt("");
			user.setPassword("password");
			userDao.addUser(user);

			Question question=new Question();
			question.setCommentCount(0);
			question.setUserId(user.getId());
			question.setCreatedDate(new Date());
			question.setStatus(0);
			question.setTitle("Title "+i);
			question.setContent("This is a test question "+i+",这是一条测试数据");

			questionDao.addQuestion(question);
		}
	}

	@Test
	public void testQuestionService() {
		Question question=new Question();
		question.setTitle("为啥么？？？");
		question.setContent("i don konw!");
		question.setStatus(0);
		question.setCreatedDate(new Date());
		question.setUserId(11);
		question.setCommentCount(0);
		List<Question> questions=questionService.getLatestQuestions(0,0,10);
		String s= JSON.toJSONString(questions);
		System.out.println(s);
	}
	@Test
	public void testPageHelper() {
		String str=indexController.MoreQuestion1(0,0,10);

	}
}
