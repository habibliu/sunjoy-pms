package com.sunjoy.system.api.factory;

import com.sunjoy.common.core.domain.R;
import com.sunjoy.system.api.RemoteParkOperationService;
import com.sunjoy.system.api.domain.PmsParkOrder;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/12/9
 */
public class RemoteParkOperationFallbackFactory implements FallbackFactory<RemoteParkOperationService> {
    @Override
    public RemoteParkOperationService create(Throwable throwable) {
        return new RemoteParkOperationService() {

            @Override
            public R<Long> createRegistedVehicleServiceOrder(PmsParkOrder parkOrder, String source) {
                return R.fail("创建登记车服务费订单失败:" + throwable.getMessage());
            }
        };
    }
}