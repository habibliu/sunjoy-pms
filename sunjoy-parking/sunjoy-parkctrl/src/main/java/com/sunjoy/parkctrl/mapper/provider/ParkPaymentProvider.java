package com.sunjoy.parkctrl.mapper.provider;

import com.sunjoy.system.api.domain.PmsParkPayment;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
public class ParkPaymentProvider {
    /**
     * 动态构造更新 SQL
     */
    public String updatePayment(PmsParkPayment payment) {
        return new SQL() {{
            UPDATE("pms_park_payment");
            if (payment.getOrderId() != null) SET("order_id = #{orderId}");
            if (payment.getTransId() != null) SET("trans_id = #{transId}");
            if (payment.getTenantId() != null) SET("tenant_id = #{tenantId}");
            if (payment.getOpuId() != null) SET("opu_id = #{opuId}");
            if (payment.getParkId() != null) SET("park_id = #{parkId}");
            if (payment.getLicensePlate() != null) SET("license_plate = #{licensePlate}");
            if (payment.getServiceId() != null) SET("service_id = #{serviceId}");
            if (payment.getOrderAmount() != null) SET("order_amount = #{orderAmount}");
            if (payment.getPaymentAmount() != null) SET("payment_amount = #{paymentAmount}");
            if (payment.getDiscountAmount() != null) SET("discount_amount = #{discountAmount}");
            if (payment.getPaymentMethods() != null) SET("payment_methods = #{paymentMethods}");
            if (payment.getPaymentChannel() != null) SET("payment_channel = #{paymentChannel}");
            if (payment.getPaymentTime() != null) SET("payment_time = #{paymentTime}");
            if (payment.getTransactionId() != null) SET("transaction_id = #{transactionId}");
            if (payment.getStatus() != null) SET("status = #{status}");
            if (payment.getDelFlag() != null) SET("del_flag = #{delFlag}");
            if (payment.getUpdateBy() != null) SET("update_by = #{updateBy}");
            if (payment.getUpdateTime() != null) SET("update_time = #{updateTime}");
            if (payment.getRemark() != null) SET("remark = #{remark}");
            WHERE("payment_id = #{paymentId}");
        }}.toString();
    }

    /**
     * 动态构造查询 SQL
     */
    public String selectPayment(PmsParkPayment payment) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_park_payment");
            if (payment.getPaymentId() != null) WHERE("payment_id = #{paymentId}");
            if (payment.getOrderId() != null) WHERE("order_id = #{orderId}");
            if (payment.getTransId() != null) WHERE("trans_id = #{transId}");
            if (payment.getTenantId() != null) WHERE("tenant_id = #{tenantId}");
            if (payment.getOpuId() != null) WHERE("opu_id = #{opuId}");
            if (payment.getParkId() != null) WHERE("park_id = #{parkId}");
            if (payment.getLicensePlate() != null) WHERE("license_plate = #{licensePlate}");
            if (payment.getServiceId() != null) WHERE("service_id = #{serviceId}");
            if (payment.getStatus() != null) WHERE("status = #{status}");
            if (payment.getDelFlag() != null) WHERE("del_flag = #{delFlag}");
        }}.toString();
    }
}