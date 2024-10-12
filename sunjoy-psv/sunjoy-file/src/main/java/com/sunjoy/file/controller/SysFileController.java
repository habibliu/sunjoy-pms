package com.sunjoy.file.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sunjoy.common.core.domain.R;
import com.sunjoy.common.core.utils.file.FileUtils;
import com.sunjoy.file.service.ISysFileService;
import com.sunjoy.system.api.domain.SysFile;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件请求处理
 * 
 * @author sunjoy
 */
@RestController
//@RequestMapping("/file")
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("/upload")
    public R<SysFile> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok(sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("/download")
    public R<ResponseEntity<Resource>> downloadFile(@RequestParam String fileName) {
        try {

            Resource resource = sysFileService.downloadFile(fileName);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            if (resource != null) {
                return R.ok(ResponseEntity.status(HttpStatus.OK)
                        .headers(headers)
                        .contentLength(resource.getFile().length())
                        .body(resource));
            }
        }catch(Exception er){
            log.error(er.getMessage());
        }
        return R.fail(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}