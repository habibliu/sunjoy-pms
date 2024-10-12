package com.sunjoy.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.sunjoy.common.core.constant.ServiceNameConstants;
import com.sunjoy.common.core.domain.R;
import com.sunjoy.system.api.domain.SysFile;
import com.sunjoy.system.api.factory.RemoteFileFallbackFactory;

/**
 * 文件服务接口
 * 通过feign的方式供调用
 * 
 * @author sunjoy
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService
{
    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 下载文件
     * @param fileName
     * @return ResponseEntity对象
     */
    @GetMapping(value = "/download")
    public R<ResponseEntity<Resource>> downloadFile(String fileName) ;
}
