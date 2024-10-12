package com.sunjoy.file.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sunjoy.file.utils.FileUploadUtils;

import java.io.File;

/**
 * 本地文件存储
 * 
 * @author Habib
 */


@Slf4j
@Primary
@Service
public class LocalSysFileServiceImpl implements ISysFileService
{
    /**
     * 资源映射路径 前缀
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址
     */
    @Value("${file.domain}")
    public String domain;
    
    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    /**
     * 本地文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception
    {
        String name = FileUploadUtils.upload(localFilePath, file);
        String url = domain + localFilePrefix + name;
        return url;
    }

    /**
     * 本地文件下载
     * @param fileName
     * @return
     * @throws Exception
     */
    @Override
    public Resource downloadFile(String fileName) {
        // 指定文件路径
        File file = new File(localFilePath+"/" + fileName); // 替换为你的文件路径

        if (!file.exists()) {
            log.error("文件:{}不存在",fileName);
            return null;
        }

        return new FileSystemResource(file);
    }

}
