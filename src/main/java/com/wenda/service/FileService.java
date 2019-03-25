package com.wenda.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class FileService implements InitializingBean {
    public void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void uploadToTencentCos(File file,String new_filename){
        File localFile = new File("src/test/resources/len5M.txt");
// 指定要上传到的存储桶
        String bucketName = "demoBucket-1250000000";
// 指定要上传到 COS 上对象键
        String key = "upload_single_demo.txt";

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
    }
    public void afterPropertiesSet() throws Exception {
        // 1 传入获取到的临时密钥 (tmpSecretId, tmpSecretKey, sessionToken)
        BasicSessionCredentials cred = new BasicSessionCredentials("a-demo-secretId", "a-demo-secretKey", "a-demo-session-token");
// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认http), 超时, 代理等 set 方法, 使用可参见源码或者接口文档 FAQ
        ClientConfig clientConfig = new ClientConfig(new Region("ap-beijing-1"));
// 3 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
// bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = "mybucket-1251668577";

    }
}
