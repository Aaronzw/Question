package com.wenda;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenda.Admin.service.PrivageService;
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
	@Autowired
	CommentDao commentDao;
	@Autowired
	JedisAdapter jedisAdapter;
	@Autowired
	LikeService likeService;
	@Autowired
	MailSender mailSender;
	@Autowired
	IndexController indexController;
	@Autowired
	ReadRecordService readRecordService;
	@Autowired
	FollowService followService;
	@Autowired
	RecommenderService recommenderService;
	@Autowired
	DynamicService dynamicService;
	@Autowired
	ReportService reportService;
	@Autowired
	SearchService searchService;
	@Autowired
	PrivageService privageService;
	@Autowired
	HostHolder hostHolder;
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

	@Test
	public void testMail(){
		Map<String,Object> model=new HashMap<>();
		model.put("userName","紫珠状锥花");
		if(mailSender.sendWithHTMLTemlate("905380097@qq.com","设计稿子","mail.html",model)){
			System.out.println("ok");
		}
	}
	@Test
	public void testq(){
//		double timeScore=followService.getFollowerTime(24,26);
		/*fasttime转date转string*/
		double timeScore=0;
		String time=WendaUtil.DateFormat(WendaUtil.longFastTime2Date((long)timeScore));
		System.out.println(time);
	}

	@Test
	public void testBrowseRecord(){
		long num=readRecordService.getBrowseCount(24,EntityType.ENTITY_QUESTION);
		List<Integer> list=readRecordService.getBrowseRecordList(24,EntityType.ENTITY_QUESTION,0,(int)num);
		long num1=readRecordService.getBrowsedCount(EntityType.ENTITY_QUESTION,116);
		List<Integer> list1=readRecordService.getBrowsedRecordList(EntityType.ENTITY_QUESTION,116,0,(int)num);
		System.out.println();
	}

	@Test
	public void testHotTOP(){

		List<Question> list=readRecordService.getHotQuestionDesc();
		for(int i=1;i<=10;i++){
			System.out.println(list.get(i));
		}
		System.out.println();
	}

	@Test
	public void testRedis() {

		readRecordService.userBrowseAdd(1, EntityType.ENTITY_QUESTION, 1);
		readRecordService.userBrowseAdd(1, EntityType.ENTITY_QUESTION, 3);

		readRecordService.userBrowseAdd(2, EntityType.ENTITY_QUESTION, 1);
		readRecordService.userBrowseAdd(2, EntityType.ENTITY_QUESTION, 2);
		readRecordService.userBrowseAdd(2, EntityType.ENTITY_QUESTION, 3);
		readRecordService.userBrowseAdd(2, EntityType.ENTITY_QUESTION, 4);

		readRecordService.userBrowseAdd(3, EntityType.ENTITY_QUESTION, 3);
		readRecordService.userBrowseAdd(3, EntityType.ENTITY_QUESTION, 4);



		Map<String,List> userSets = new HashMap();
		List<Integer> list;
		for (int i = 1; i <=3; i++) {//user i
			list = new ArrayList<>();
			long count=readRecordService.getBrowseCount(i,EntityType.ENTITY_QUESTION);
			list=readRecordService.getBrowseRecordList(i,EntityType.ENTITY_QUESTION,0,(int)count);
			userSets.put(String.valueOf(i),list);
		}

		Map<String,List> itemSets = new HashMap();
		for (int i = 1; i <=4; i++) {//物品i
			list = new ArrayList<>();
			long count=readRecordService.getBrowsedCount(EntityType.ENTITY_QUESTION,i);
			list=readRecordService.getBrowsedRecordList(EntityType.ENTITY_QUESTION,i,0,(int)count);
			itemSets.put(String.valueOf(i),list);
		}

		double []ranku=new double[5];
		double []rankitem=new double[5];
		Arrays.fill(rankitem,0);
		Arrays.fill(ranku,0);
		ranku[0]=1;
		for(int time=0;time<1;time++){
			for(int i=1;i<4;i++){
				if(ranku[i]!=0){
					//list为与相关的节点
					list=userSets.get(String.valueOf(i));
					for(Integer k :list){
						rankitem[k]=rankitem[k]+ranku[i]/list.size();
					}
				}
			}
			for(int i=1;i<5;i++){
				if(rankitem[i]!=0){
					//list为与i相关的节点
					list=itemSets.get(String.valueOf(i));
					for(Integer k :list){
						ranku[k]=ranku[k]+rankitem[i]/list.size();
					}
				}
			}
		}
		System.out.println(rankitem);

	}
	@Test
	public  void testREcomender(){
		List<Integer> userIds=userService.getUserIdList();
		List<Integer> itemIds=questionService.getQuestionIdList();
		HashMap<Integer,HashMap<Integer,Integer>> usermatrix=recommenderService.getMatrixBasedOnUsers(userIds);
		HashMap<Integer,HashMap<Integer,Integer>> itemMatrix=recommenderService.getMatrixBasedOnItems(itemIds);
		List<Integer> result=recommenderService.getRecListOnUserIdByTwoPartMethod(1,userIds,itemIds,usermatrix,itemMatrix);

//		List<Integer> result=recommenderService.getRecomenderItemsForUser(1);
		System.out.println(result);
	}

	@Test
	public void testSearch(){
		PageHelper.startPage(1,5);
		List<Question> questionList=questionDao.searchQuestions("s");
//		List<Question> questionList=questionDao.selectLatestQuestionsPageHelper(0);
		PageInfo<Question> pageInfo=new PageInfo<>(questionList);
		System.out.println();
	}

	@Test
	public  void  testFeed(){
		User user=userService.getUser(1);
		User loguser=userService.getUser(30);
		hostHolder.setUser(loguser);
		HashMap result=new HashMap();
		result=privageService.updateUserPri(1,PrivageLevel.pri_admin);
		user=userService.getUser(1);
		System.out.println(user);
	}

	@Test
	public void testReport(){
		List<User> userList=userDao.getUsersByStatus(2);
		System.out.println(userList.size());
	}

	@Test
	public void testForItemRecommender(){
//		readRecordService.userBrowseAdd(1,EntityType.ENTITY_QUESTION,1);
//		readRecordService.userBrowseAdd(1,EntityType.ENTITY_QUESTION,2);
//		readRecordService.userBrowseAdd(1,EntityType.ENTITY_QUESTION,3);
//		readRecordService.userBrowseAdd(2,EntityType.ENTITY_QUESTION,2);
//		readRecordService.userBrowseAdd(2,EntityType.ENTITY_QUESTION,4);
//		readRecordService.userBrowseAdd(4,EntityType.ENTITY_QUESTION,2);
//		readRecordService.userBrowseAdd(4,EntityType.ENTITY_QUESTION,4);
		List<Integer> userList=userService.getUserIdList();
		List<Integer> questionList=questionService.getQuestionIdList();

		HashMap<Integer,HashMap<Integer,Integer>> usermatrix=recommenderService.getMatrixBasedOnUsers(userList);

		HashMap<Integer,HashMap<Integer,Integer>> questionmatrix=recommenderService.getMatrixBasedOnItems(questionList);

		List<Integer> recomenderList=recommenderService.getRecListOnItemIdByTwoPartMethod(2,userList,questionList,usermatrix,questionmatrix);

//		System.out.println(recomenderList);
		System.out.println("ok");

	}
	@Test
	public void registerUser(){
		for(int i=0;i<20;i++)
		{
//			String name=(char)(96+i)+""+(char)(96+i);
//			userService.register(name,name);
			String url=WendaUtil.getRandonHeadUrl();
			System.out.println(url);
		}
	}
}
