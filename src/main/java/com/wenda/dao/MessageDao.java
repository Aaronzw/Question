package com.wenda.dao;

import com.wenda.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDao {
    String TABLE_NAME=" message ";
    String INSERT_FIELDS=" from_id,to_id,content,has_read,conversation_id,created_date ";
    String SELECT_FIELDS=" id ,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,")values(#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);
    //消息详情列表
    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} order by created_date desc " })
    List<Message> getConversationDetail(@Param("conversationId")String conversationId);
    //消息列表
    List<Message> getConversationList(@Param("userId")int userId);
    //消息列表未读数目
    @Select({"select count(id) from",TABLE_NAME,"where conversation_id=#{conversationId} and has_read=0 and to_id=#{userId}"})
    int getConversationUnreadCount(@Param("userId")int userId,@Param("conversationId")String conversationId);

    //根据id查询消息详情
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id} "})
    Message selectMessageById(@Param("id")int id);
    //根据id查询消息详情
    Message selectById(int id);
    //更新消息状态
    @Update({"update ",TABLE_NAME,"set has_read=#{hasRead} where id=#{id}"})
    int setMsgHasRead(@Param("id")int id,@Param("hasRead")int hasRead);
}
