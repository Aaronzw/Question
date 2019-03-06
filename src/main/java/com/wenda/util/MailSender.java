package com.wenda.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {
    Logger logger= LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;
    @Autowired
    VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemlate(String to, String subject, String template, Map<String,Object> model){
        try {
            String nick= MimeUtility.encodeText("问答");
            InternetAddress from = new InternetAddress("13067201683@163.com");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            //mimeMessageHelper.setText("你在干嘛呢",false);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        }catch (Exception e){
            logger.error("发送邮件失败,"+e.getMessage());
            return false;
        }

    }
    @Override
    public void afterPropertiesSet() throws Exception{
       mailSender=new JavaMailSenderImpl();
       mailSender.setUsername("13067201683@163.com");
       //授权码
       mailSender.setPassword("testmail123");
       mailSender.setHost("smtp.163.com");
       mailSender.setPort(465);
       mailSender.setProtocol("smtps");
       Properties javaProperties=new Properties();
       javaProperties.put("mail.smtp.ssl.enable",true);
       mailSender.setJavaMailProperties(javaProperties);
    }

}
