package com.wenda.service;

import com.wenda.dao.ReportDao;
import com.wenda.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReportService {
    @Autowired
    ReportDao reportDao;
    //发起举报，实体可以是问题，回答，用户
    public int addReport(Report report){
        return reportDao.addReport(report);
    }
    //处理举报记录，记录id，处理人，处理结果，处理时间
    public boolean dealReport(int id, int userId, int resultCode, Date date){
        Report report=reportDao.getReportById(id);
        int ret1=reportDao.updateReportDealer(id,userId);
        int ret2=reportDao.updateReportStatus(id,resultCode);
        int ret3=reportDao.updateReportDealDate(id,date);
        return (ret1+ret2+ret3)>=3;
    }
    //获取某一用户的举报记录
    public Report getReportById(int id){
        return reportDao.getReportById(id);
    }
}
