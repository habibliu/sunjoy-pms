package com.sunjoy.file.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 * 
 * @author sunjoy
 */
public interface ISysFileService
{
    /**
     * 文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception;

    /**
     * 下载文件接口
     * @param fileName
     * @return
     * @throws Exception
     */
    public Resource downloadFile(String fileName) ;
}
