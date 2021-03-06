package com.wenda.service;

import com.wenda.dao.LoginTicketDao;
import com.wenda.dao.UserDao;
import com.wenda.model.Constant;
import com.wenda.model.LoginTicket;
import com.wenda.model.PrivageLevel;
import com.wenda.model.User;
import com.wenda.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.Ticket;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger=LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    public Map<String,Object> register(String name,String password){
        Map map=new HashMap();
        if(StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user=userDao.selectByName(name);
        if(user!=null){
            map.put("msg","用户名已被注册");
            return map;
        }

        user=new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
//        String head_url= String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        String head_url="http://wenda-question-1256798108.cos.ap-guangzhou.myqcloud.com/headPic/1558233336728.png";
        user.setHeadUrl(head_url);
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDao.addUser(user);

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> login(String name,String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空！");
            return map;
        }

        User user=userDao.selectByName(name);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).endsWith(user.getPassword()))
        {
            map.put("msg","密码错误");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).endsWith(user.getPassword()))
        {
            map.put("msg","密码错误");
            return map;
        }
        if(user.getStatus()==Constant.User_banned){
            map.put("msg","该用户账号被封禁");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+3600*24*7);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUser(int id){
        return userDao.selectById(id);
    }

    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }

    public List<User> getUserPriType(int pri){
        return userDao.getUsersByPri(pri);
    }

    public List<User> UserIdsToUserList(List<Integer> integerList){
        List<User> userList=new ArrayList<>();
        for(Integer i :integerList){
            User user=userDao.selectById(i);
            if(user==null)
                continue;
            userList.add(user);
        }
        return userList;
    }

    public  List<Integer> getUserIdList(){
        List<Integer> idlist=new ArrayList<>();
        List<User> userList=userDao.getUserList();
        for(User user:userList){idlist.add(user.getId());
        }
        return idlist;
    }

    public boolean checkPass(int userId,String password){
        User user=getUser(userId);
        if(user==null){
            return false;
        }
        String md5_pass=WendaUtil.MD5(password+user.getSalt());
        return md5_pass.equals(user.getPassword());
    }

    public Map<String,Object> adlogin(String name,String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(name)){
            map.put("msg","管理员名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空！");
            return map;
        }

        User user=userDao.selectByName(name);
        if(user==null){
            map.put("msg","管理员账号不存在");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).endsWith(user.getPassword()))
        {
            map.put("msg","密码错误");
            return map;
        }
        if(user.getPriLv()==PrivageLevel.pri_user){
            map.put("msg","该用户没有管理员权限");
            return map;
        }
        if(user.getStatus()==Constant.User_banned){
            map.put("msg","该用户被封禁，如有疑问请联系管理员");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public List<User> searchUserName(String keyWord){
        return userDao.getUsersByName(keyWord);
    }

    public int updateHeadUrl(int userId,String newUrl){
        return userDao.updateHeadUrl(userId,newUrl);
    }

    public int updatePassword(int userId,String password){
        User user=getUser(userId);
        String md5_pass=WendaUtil.MD5(password+user.getSalt());
        if(md5_pass==user.getPassword())
            System.out.println("ok");
        return userDao.updatepassword(userId,md5_pass);
    }

    public int upUserPri(int userId,int pri){
        return userDao.updatePri(userId,pri);
    }

    public int updateUserStatus(int userId,int status){
        return userDao.updateStatus(userId,status);
    }

    public List<User> getAllUsers(int status){
            //status 0 正常用户列表 ，status 1 被禁用户列表
            //status 2 获取全部用户列表
            return userDao.getUsersByStatus(status);
    }
}
