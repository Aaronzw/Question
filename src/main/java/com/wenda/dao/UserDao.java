package com.wenda.dao;

import com.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url , pri_lv , status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl},#{priLv},#{status} )"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    //    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
//    User selectByName(String name);
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where pri_lv=#{pri_lv}"})
    List<User> getUsersByPri(int pri_lv);
    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

    //账号封禁
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Update({"update ", TABLE_NAME, " set head_url=#{url} where id=#{id}"})
    int updateHeadUrl(@Param("id") int id,@Param("url") String url);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    int updatepassword(@Param("id") int id,@Param("password") String password);

    //修改权限等级
    @Update({"update ", TABLE_NAME, " set pri_lv=#{pri_lv} where id=#{id}"})
    int updatePri(@Param("id") int id,@Param("pri_lv") int pri_lv);

    /*获取用户列表+name模糊查询*/
    List<User> getUsersByName(@Param("user_name")String user_name);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME})
    List<User> getUserList();

    List<User> getUsersByStatus(@Param("status") int status);
}
