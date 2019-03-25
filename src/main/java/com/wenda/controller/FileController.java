package com.wenda.controller;

import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.service.FileService;
import com.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@Controller
public class FileController {
    @Autowired
    FileService fileService;
    @RequestMapping(path ={"/upload/"},method = {RequestMethod.POST})
    /*multipartFile转file，再上传到腾讯云，再删除file，以免占空间*/
    public String likeComment(@RequestParam("file")MultipartFile multipartFile) throws Exception{
        File toFile=null;
        if(multipartFile.getSize()<=0||multipartFile.equals("")){
            return null;
        }else {
            InputStream ins=null;
            ins=multipartFile.getInputStream();
            toFile = new File(multipartFile.getOriginalFilename());
            fileService.inputStreamToFile(ins,toFile);
            toFile.delete();
            ins.close();
        }
        return "redirect:/test";
    }

}

