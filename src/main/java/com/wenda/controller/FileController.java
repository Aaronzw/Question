package com.wenda.controller;

import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.service.FileService;
//import com.wenda.service.TencentCloudService;
import com.wenda.service.TencentCloudService;
import com.wenda.service.UserService;
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
    @Autowired
    TencentCloudService tencentCloudService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path ={"/uploadHeadPic/"},method = {RequestMethod.POST})
    @ResponseBody
    /*multipartFile转file，再上传到腾讯云，再删除file，以免占空间*/
    public String likeComment(@RequestParam("file")MultipartFile multipartFile) throws Exception{
        if(hostHolder.getUser()==null)
            return WendaUtil.getJSONString(-1);
        File toFile=null;
        if(multipartFile.getSize()<=0||multipartFile.equals("")){
            return WendaUtil.getJSONString(1,"fail");
        }else {
            InputStream ins=null;
            ins=multipartFile.getInputStream();
            toFile = new File(multipartFile.getOriginalFilename());
            fileService.inputStreamToFile(ins,toFile);
            String url=tencentCloudService.uploadHeadPic(toFile,toFile.getName());
            userService.updateHeadUrl(hostHolder.getUser().getId(),url);
            ins.close();
            toFile.delete();
            return WendaUtil.getJSONString(0,url);
        }
    }

}

