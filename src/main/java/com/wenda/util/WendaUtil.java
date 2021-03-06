package com.wenda.util;

import com.alibaba.fastjson.JSONObject;
import com.wenda.model.User;
import com.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class WendaUtil {
    private static final Logger logger = LoggerFactory.getLogger(WendaUtil.class);
    public static int ANONYMOUS_USERID=3;
    public static int SYSTEM_USERID=4;
    /*性别常量*/
    public static int SEX_UNKNOW=0;
    public static int SEX_MALE=1;
    public static int SEX_FEMALE=2;


    //分页工具配置
    private static int pageNum=1;
    private static int pageSize=5;
    public static String MD5(String key) {

        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    public static String getJSONString(int code){
        JSONObject json=new JSONObject();
        json.put("code",code);
        return json.toString();
    }
    public static String getJSONString(int code,String msg){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toString();
    }
    public static String getJSONString(int code, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toString();
    }

    //conversationId "12_14",localUserId 14，默认参数合法
    public static int getChatTarget(String converationId,int localUserId){
        String[] ids=converationId.split("_");
        if(ids.length<2)
            return -1;
        else {
            if(Integer.parseInt(ids[0])==localUserId)
                return Integer.parseInt(ids[1]);
            else if(Integer.parseInt(ids[1])==localUserId){
                return Integer.parseInt(ids[0]);
            }else
                return -1;
        }
    }
    public static Date longFastTime2Date(double fastTime) {
        return new Date((long)fastTime );
    }

    /*重载函数，Date to string*/
    public static String DateFormat(Date date){
        String formatPattern="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf =new SimpleDateFormat(formatPattern);
        String str = sdf.format(date);
        return str;
    }
    public static String DateFormat(Date date,String formatPattern){
        SimpleDateFormat sdf =new SimpleDateFormat(formatPattern);
        Date d= new Date();
        String str = sdf.format(date);
        return str;
    }

    //根据分页截取列表list
    public static <T> ArrayList<T> pageHelper(List<T> data) {
        HashMap pageInfo=new HashMap();
        ArrayList<T> list=new ArrayList<>();
        int size=data.size();
        for(int i=(pageNum-1)*pageSize;i<pageNum*pageSize;i++){
            if(i<size)
                list.add(data.get(i));
        }
        return list;
    }
    public static void pageStart(int offset,int limit){
        //页数从1开始,pageSize
        if(limit>0&&offset>0){
            pageNum=offset;
            pageSize=limit;
        }
    }

    public static String getRandonHeadUrl(){
        Random random=new Random();
        ArrayList<String> urlList=new ArrayList();
        String commom="https://wenda-question-1256798108.cos.ap-guangzhou.myqcloud.com/headPic/";
        urlList.add(commom+"1553930223072.png");
        urlList.add(commom+"1553930389088.png");
        urlList.add(commom+"1553930593079.png");
        urlList.add(commom+"1553931436068.png");
        urlList.add(commom+"1553931676630.png");
        urlList.add(commom+"1554034581045.png");

        return urlList.get(random.nextInt(urlList.size()));
    }
}
