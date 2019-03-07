package com.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;
import sun.security.util.Password;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger=LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    public static void print(int key,Object obj){
        System.out.println(key+","+obj.toString());
    }
//    public static void main(String []argv){
//        JedisShardInfo shardInfo = new JedisShardInfo("redis://139.199.205.104:6379/1");//这里是连接的本地地址和端口
//        shardInfo.setPassword("213546");//这里是密码
//        Jedis jedis = new Jedis(shardInfo);
//        jedis.connect();
//        jedis.flushAll();
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //项目用到的redis配置信息
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        String host="139.199.205.104";
        int port=6379;
        int timeout=2000;
        String password="213546";
        pool = new JedisPool(jedisPoolConfig,host,port,timeout,password);
    }

    /*将val加入key对应的一个集合
        return:被成功加入的元素的数量
    */
    public long sadd(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }

    /*将value移除key对应的一个集合
        return:被成功移除的元素的数量
    */
    public long srem(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }
    /*
        return:key对应集合的元素数量
    */
    public long scard(String key){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }
    /*
        判断value是否是key集合成员
        return:true false
    */
    public boolean sismenber(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return false;
    }
    /*
        将移出并获取列表的最后一个元素
        return:
    */
    public List<String> brpop(int timeout, String key){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return null;
    }
    /*
        将移出并获取列表的最后一个元素
        return:
    */
    public long lpush(String key, String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }
    /*
        Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。
        其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
        你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
        start<=i<=end
    */
    public List<String> lrange(String key, int start,int end){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.lrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return null;
    }
    /*
       向有序集合添加一个或多个成员，或者更新已存在成员的分数
   */
    public long zadd(String key, double score,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zadd(key,score,value);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }
    /*
      向有序集合添加一个或多个成员，或者更新已存在成员的分数
  */
    public long zrem(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zrem(key,value);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }
    /*
      返回有序集中，指定区间内的成员。
      其中成员的位置按分数值递增(从小到大)来排序。
      具有相同分数值的成员按字典序(lexicographical order )来排列
    */
    public Set<String> zrange(String key,int start,int end){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return null;
    }
    /*
     有序集合的元素从第start个到第end个
     返回有序集中，指定区间内的成员。
     其中成员的位置按分数值递减(从大到小)来排列
    */
    public Set<String> zrevrange(String key,int start,int end){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zrevrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return null;
    }
    /*返回有序集的元素数量*/
    public long zcard(String key){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }
    /*返回有序集中，成员的分数值
    */
    public Double zscore(String key, String member){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zscore(key,member);
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return null;
    }

    public  Jedis getJedis(){
        return pool.getResource();
    }
    /*事务操作 */
    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        }catch (Exception e){
            logger.error("发生异常！获取redis 连接池资源失败！"+e.getMessage());
        }finally {}
        return null;
    }
    /**/
    public List<Object> exec(Transaction tx,Jedis jedis){
        try{
            return tx.exec();
        }catch (Exception e){
            logger.error("发生异常！执行事务失败！"+e.getMessage());
            tx.discard();
        }finally {
            if(tx!=null){
                try {
                    tx.close();
                }catch (Exception e){
                    logger.error("发生异常！关闭事务失败！"+e.getMessage());
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }
}
