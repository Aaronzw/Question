package com.wenda.dao;

import com.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url , pri_lv ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl},#{priLv})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

//    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
//    User selectByName(String name);
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

    //账号封禁
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Update({"update ", TABLE_NAME, " set head_url=#{url} where id=#{id}"})
    int updateHeadUrl(@Param("id") int id,@Param("url") String url);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    int updatepassword(@Param("id") int id,@Param("password") String password);
    /*获取用户列表+name模糊查询*/
    List<User> getUsers(@Param("user_name")String user_name);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME})
    List<User> getUserList();
}
