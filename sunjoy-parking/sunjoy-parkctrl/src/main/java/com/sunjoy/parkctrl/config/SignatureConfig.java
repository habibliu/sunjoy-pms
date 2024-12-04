package com.sunjoy.parkctrl.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 是否要验签，主要是用于测试与生产环境时的开关
 *
 * @author Habib
 * @date 2024/11/27
 */
@Getter
@Component
public class SignatureConfig {
    @Value("${signature.valid.wechat:true}")
    private boolean wechatValid;

    @Value("${signature.valid.alipay:true}")
    private boolean alipayValid;

}