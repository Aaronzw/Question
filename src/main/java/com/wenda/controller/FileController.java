package com.wenda.controller;

import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.util.WendaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class FileController {
    @RequestMapping(path ={"/upload/"},method = {RequestMethod.POST})
    @ResponseBody
    public String likeComment(@RequestParam("file")MultipartFile file){
        int a=1+1;
        return "hi";
    }

}

