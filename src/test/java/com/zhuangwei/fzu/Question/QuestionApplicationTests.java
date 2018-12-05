package com.zhuangwei.fzu.Question;

import com.zhuangwei.fzu.Question.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Question.class)
@WebAppConfiguration
public class QuestionApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("hi");
	}

}
