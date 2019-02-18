package com.wenda;


import com.alibaba.fastjson.JSON;
import com.wenda.controller.IndexController;
import com.wenda.controller.QuestionController;
import com.wenda.dao.*;
import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import org.aspectj.bridge.MessageWriter;
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
	CommentService commentService;

	@Autowired
	MessageDao messageDao;
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
	@Autowired
	CommentDao commentDao;
	@Test
	public void testQuestionDao() {
//		Question question=new Question();
//		question.setCommentCount(0);
//		question.setUserId(1);
//		question.setCreatedDate(new Date());
//		question.setStatus(0);
//		question.setTitle("Title "+1);
//		question.setContent("This is a test question "+1+",这是一条测试数据");
//
//		int val=questionDao.addQuestion(question);
		Comment comment=new Comment();
		comment.setUserId(1);
		comment.setStatus(0);
		comment.setEntityType(EntityType.ENTITY_QUESTION);
		comment.setCreatedDate(new Date());
		comment.setEntityId(1);
		comment.setContent("test comment");
		commentDao.addComment(comment);
		System.out.println(comment.getId());
	}
	@Test
	public void testMessage() {
		Random random=new Random();
//		String str=indexController.MoreQuestion1(0,0,10);
		for(int i=0;i<10;i++){
			Message message=new Message();
			if(random.nextInt(4)%4==0) {
				message.setFromId(11);
				message.setToId(12);
			}else if(random.nextInt(4)%4==1){
				message.setFromId(11);
				message.setToId(13);
			}else if(random.nextInt(4)%4==2){
				message.setFromId(12);
				message.setToId(11);
			}if(random.nextInt(4)%4==1){
				message.setFromId(13);
				message.setToId(11);
			}
			message.setCreatedDate(new Date());
			message.setHasRead(0);
			String conversationId=message.getConversationId();
			message.setConversationId(conversationId);
			message.setContent("from "+message.getFromId()+",to "+message.getToId());
			try {
				messageDao.addMessage(message);
			}catch (Exception e){
				System.out.println(e);
			}
		}
	}
	@Test
	public void testq(){
		List<Message> messageList=messageDao.getConversationList(1);
		int num=messageDao.getConversationUnreadCount(1,"1_2");
		Message message=messageDao.selectMessageById(113);

		Question question=questionDao.getById(1);
		int c=1+1;
	}
}
