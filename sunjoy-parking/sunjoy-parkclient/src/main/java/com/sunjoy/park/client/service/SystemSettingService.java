package com.sunjoy.park.client.service;

import com.sunjoy.park.client.config.DaemonProcess;
import com.sunjoy.park.client.entity.PlatformInfo;
import com.sunjoy.park.client.repository.SystemSettingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/29
 */
@Slf4j
@Service
public class SystemSettingService {
    @Autowired
    private SystemSettingRepository systemSettingRepository;

    public void saveSystemSetting(PlatformInfo platformInfo) {
        systemSettingRepository.save(platformInfo);
        log.info("保存系统配置参数成功!");
        //todo 与平台取得联系，获取本终端的
        if (platformInfo.getPlatformUrl() != null && !platformInfo.getPlatformUrl().isEmpty()
            && platformInfo.getUserName() != null && !platformInfo.getUserName().isEmpty()
            && platformInfo.getPassword() != null && !platformInfo.getPassword().isEmpty()) {
            DaemonProcess daemonProcess = DaemonProcess.getInstance(platformInfo.getPlatformUrl(), platformInfo.getUserName(), platformInfo.getPassword());
            daemonProcess.start();
        }
    }

    public PlatformInfo getSystemSetting() {
        return this.systemSettingRepository.findAll().stream().findFirst().orElse(null);
    }
}