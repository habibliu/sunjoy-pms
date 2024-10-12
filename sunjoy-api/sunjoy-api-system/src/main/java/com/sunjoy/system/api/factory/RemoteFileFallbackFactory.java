package com.sunjoy.system.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.sunjoy.common.core.domain.R;
import com.sunjoy.system.api.RemoteFileService;
import com.sunjoy.system.api.domain.SysFile;

/**
 * 文件服务降级处理
 * 
 * @author sunjoy
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService()
        {
            @Override
            public R<SysFile> upload(MultipartFile file)
            {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<ResponseEntity<Resource>> downloadFile(String fileName) {
                return R.fail("下载文件失败:" + throwable.getMessage());
            }
        };
    }
}
