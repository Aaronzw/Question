package com.wenda;


import com.wenda.model.User;
import com.wenda.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionApplication.class)
@WebAppConfiguration
public class QuestionApplicationTests {
	@Autowired
	UserDao userDao;

	//插入假数据
	@Test
	public void contextLoads() {
	User user=new User();
		user.setHeadUrl("oooo");
		user.setName("i2i");
		user.setPassword("sss");
		user.setSalt("sss");
		userDao.addUser(user);
	}

}