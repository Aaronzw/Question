package com.wenda.service;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

@Service
public class TencentCloudService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(TencentCloudService.class);
    /*镜像存储文件夹*/
    private String fileFolder="headPic/";
    private static String accessKey="AKIDGKAl7Emj9pHVUFf23oNhlZ8RVJrDsAl7";
    private static String secretKey="FF2a1DArIUQ6XorgGbhYBcvznMgkZ8Ra";
    private static COSCredentials cred=null;
    private static String bucketName = "wenda-question-1256798108";

    private static ClientConfig clientConfig=null;
    private static COSClient cosClient = null;
    static {
        cred = new BasicCOSCredentials(accessKey,"FF2a1DArIUQ6XorgGbhYBcvznMgkZ8Ra");
        // 2 设置bucket的区域, COS地域的简称请参照  https://cloud.tencent.com/document/product/436/6224
        clientConfig = new ClientConfig(new Region("ap-guangzhou"));
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        COSCredentials cred = new BasicCOSCredentials(accessKey,secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照
        // https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
    }
    public URL uploadFile(File file,String new_name){
        // 3 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
        String newFileName = fileFolder+new Date().getTime() + ".png";
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        // 指定要上传到 COS 上的路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newFileName,file);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
        URL url = cosClient.generatePresignedUrl(bucketName, new_name, expiration);
        return url;
    }

}
