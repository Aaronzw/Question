package com.wenda.controller;

import com.wenda.model.EntityType;
import com.wenda.model.Question;
import com.wenda.model.ViewObject;
import com.wenda.service.ReadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HotController {
    @Autowired
    ReadRecordService readRecordService;

    @RequestMapping(path = {"/hottop"},method = {RequestMethod.POST,RequestMethod.GET})
    public String latest(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        model.addAttribute("hot_ques",getTopQues(20));
        return "hot_top";
    }
    public List<ViewObject> getTopQues(int topNum){
        List<ViewObject> vos=new ArrayList<>();
        List<Question> questionList=readRecordService.getHotQuestionDesc();
        for(int i=1;i<=topNum;i++){
            ViewObject vo=new ViewObject();
            vo.set("browseNum",readRecordService.getBrowsedCount(EntityType.ENTITY_QUESTION,questionList.get(i-1).getId()));
            vo.set("question",questionList.get(i-1));
            vos.add(vo);
        }
        return vos;
    }
}
