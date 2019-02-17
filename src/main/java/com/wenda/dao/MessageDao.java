package com.wenda.dao;

import com.wenda.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDao {
    String TABLE_NAME=" message ";
    String INSERT_FIELDS=" from_id,to_id,content,has_read,conversation_id,created_date ";
    String SELECT_FIELDS=" id "+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,")values(#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);
    //消息详情列表
    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} order created_date desc " })
    List<Message> getConversationDetail(@Param("conversationId")String conversationId);
    //消息列表
    @Select({"select", INSERT_FIELDS,", count(id) ","FROM (select * from ",TABLE_NAME ,"where to_id=#{userId} or from_id=#{userId} ORDER BY created_date desc)tt  GROUP BY conversation_id ORDER BY created_date desc"})
    List<Message> getConversationList(@Param("userId")int userId);
    //消息列表未读数目
    @Select({"select count(id) from",TABLE_NAME,"where conversation_id=#{conversationId} and has_read=0 and to_id=#{userId}"})
    int getConversationUnreadCount(@Param("userId")int userId,@Param("conversationId")String conversationId);


    //此处bug，查询的message fromId被映射为查询结果的id，原因不明，mapper.xml写法无此问题
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id} "})
    Message selectMessageById(@Param("id")int id);

    Message selectById(int id);
}
