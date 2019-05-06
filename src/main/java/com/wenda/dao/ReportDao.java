package com.wenda.dao;

import com.wenda.model.Question;
import com.wenda.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
@Mapper
public interface ReportDao {
    String TABLE_NAME = " report ";
    String INSERT_FIELDS = " user_id , reason ,entity_id ,entity_type , created_date, deal_date ,deal_status ,dealer_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    int addReport(Report report);

    List<Report> getReportType(@Param("deal_status")int deal_status);

    Report getReportById(@Param("id")int id);

    List<Report> getUserReportList(@Param("user_id")int user_id);
    @Update({"update ", TABLE_NAME, " set deal_status = #{deal_status} where id=#{id}"})
    int updateReportStatus(@Param("id") int id, @Param("deal_status") int deal_status);

    @Update({"update ", TABLE_NAME, " set dealer_id = #{dealer_id} where id=#{id}"})
    int updateReportDealer(@Param("id") int id, @Param("dealer_id") int dealer_id);

    @Update({"update ", TABLE_NAME, " set deal_date = #{deal_date} where id=#{id}"})
    int updateReportDealDate(@Param("id") int id, @Param("deal_date") Date deal_date);


    @Select({"select ",SELECT_FIELDS, " from ", TABLE_NAME, " where deal_status=#{status} and entity_type=#{entityType}"})
    List<Report> getQuestionListByStatus(@Param("status") int status, @Param("entityType") int entityType);
}
