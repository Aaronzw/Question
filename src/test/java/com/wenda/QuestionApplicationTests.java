package com.wenda;


import com.alibaba.fastjson.JSON;
import com.wenda.controller.IndexController;
import com.wenda.controller.QuestionController;
import com.wenda.dao.*;
import com.wenda.model.*;
import com.wenda.service.*;
import com.wenda.util.JedisAdapter;
import com.wenda.util.MailSender;
import com.wenda.util.WendaUtil;
import org.aspectj.bridge.MessageWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;

import java.util.*;

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
	@Autowired
	JedisAdapter jedisAdapter;
	@Autowired
	LikeService likeService;
	@Autowired
	MailSender mailSender;

	@Test
	public void testMail(){
		Map<String,Object> model=new HashMap<>();
		model.put("userName","紫珠状锥花");
		if(mailSender.sendWithHTMLTemlate("905380097@qq.com","设计稿子","mail.html",model)){
			System.out.println("ok");
		}


	}
	@Autowired
	IndexController indexController;
	@Autowired
	ReadRecordService readRecordService;
	@Test
    public void testq(){
//		boolean ok=readRecordService.AssertHasBrowse(11,EntityType.ENTITY_QUESTION,15);
		List<Comment> commentList=commentService.getLatestAnswers(0);
		System.out.println(commentList);
    }
}
