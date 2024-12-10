package com.sunjoy.system.api;

import com.sunjoy.common.core.constant.SecurityConstants;
import com.sunjoy.common.core.constant.ServiceNameConstants;
import com.sunjoy.common.core.domain.R;
import com.sunjoy.system.api.domain.PmsParkOrder;
import com.sunjoy.system.api.factory.RemoteParkOperationFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 车场运营Feign服务接口
 *
 * @author Habib
 * @date 2024/12/8
 */
@FeignClient(contextId = "remoteParkOperationService", value = ServiceNameConstants.PARKCTRL_SERVICE, fallbackFactory = RemoteParkOperationFallbackFactory.class)
public interface RemoteParkOperationService {
    /**
     * 创建登记车辆的服务订单
     *
     * @param parkOrder 车场服务订单
     * @param source    请求来源
     * @return 结果
     */
    @PostMapping("/parkorder/registed")
    public R<Long> createRegistedVehicleServiceOrder(@RequestBody PmsParkOrder parkOrder, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}