package com.sunjoy.parkctrl.mapper.provider;

import com.sunjoy.system.api.domain.PmsParkOrder;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
public class ParkOrderProvider {

    /**
     * 动态构造更新 SQL
     */
    public String updateParkOrder(PmsParkOrder parkOrder) {
        return new SQL() {{
            UPDATE("pms_park_order");
            if (parkOrder.getOrderType() != null) SET("order_type = #{orderType}");
            if (parkOrder.getTransId() != null) SET("trans_id = #{transId}");
            if (parkOrder.getTenantId() != null) SET("tenant_id = #{tenantId}");
            if (parkOrder.getOpuId() != null) SET("opu_id = #{opuId}");
            if (parkOrder.getParkId() != null) SET("park_id = #{parkId}");
            if (parkOrder.getParkName() != null) SET("park_name = #{parkName}");
            if (parkOrder.getVehicleId() != null) SET("vehicle_id = #{vehicleId}");
            if (parkOrder.getLicensePlate() != null) SET("license_plate = #{licensePlate}");
            if (parkOrder.getServiceId() != null) SET("service_id = #{serviceId}");
            if (parkOrder.getServiceName() != null) SET("service_name = #{serviceName}");
            if (parkOrder.getStartTime() != null) SET("start_time = #{startTime}");
            if (parkOrder.getEndTime() != null) SET("end_time = #{endTime}");
            if (parkOrder.getPrice() != null) SET("price = #{price}");
            if (parkOrder.getPriceUnit() != null) SET("priceUnit = #{priceUnit}");
            if (parkOrder.getPriceQuantity() != null) SET("priceQuantity = #{priceQuantity}");
            if (parkOrder.getBillingAmount() != null) SET("billing_amount = #{billingAmount}");
            if (parkOrder.getRealAmount() != null) SET("real_amount = #{realAmount}");
            if (parkOrder.getFreeAmount() != null) SET("free_amount = #{freeAmount}");
            if (parkOrder.getOrderDetail() != null) SET("order_detail = #{orderDetail}");
            if (parkOrder.getStatus() != null) SET("status = #{status}");
            if (parkOrder.getDelFlag() != null) SET("del_flag = #{delFlag}");
            if (parkOrder.getUpdateBy() != null) SET("update_by = #{updateBy}");
            if (parkOrder.getUpdateTime() != null) SET("update_time = #{updateTime}");
            if (parkOrder.getRemark() != null) SET("remark = #{remark}");
            WHERE("order_id = #{orderId}");
        }}.toString();
    }

    /**
     * 动态构造查询 SQL
     */
    public String selectParkOrder(PmsParkOrder parkOrder) {
        return new SQL() {{
            SELECT("order_id,order_type, trans_id, tenant_id, opu_id, park_id, park_name," +
                   "vehicle_id, license_plate, service_id, service_name, start_time, end_time, price,price_unit,price_quantity, billing_amount, " +
                   " real_amount, free_amount,  status, del_flag, create_by, create_time," +
                   "update_by, update_time");
            FROM("pms_park_order");
            if (parkOrder.getOrderId() != null) WHERE("order_id = #{orderId}");
            if (parkOrder.getOrderType() != null) WHERE("order_type = #{orderType}");
            if (parkOrder.getTransId() != null) WHERE("trans_id = #{transId}");
            if (parkOrder.getTenantId() != null) WHERE("tenant_id = #{tenantId}");
            if (parkOrder.getOpuId() != null) WHERE("opu_id = #{opuId}");
            if (parkOrder.getParkId() != null) WHERE("park_id = #{parkId}");
            if (parkOrder.getVehicleId() != null) WHERE("vehicle_id = #{vehicleId}");
            if (parkOrder.getLicensePlate() != null) WHERE("license_plate = #{licensePlate}");
            if (parkOrder.getServiceId() != null) WHERE("service_id = #{serviceId}");
            if (parkOrder.getServiceName() != null) WHERE("service_name = #{serviceName}");
            if (parkOrder.getStartTime() != null) WHERE("start_time = #{startTime}");
            if (parkOrder.getEndTime() != null) WHERE("end_time = #{endTime}");
            if (parkOrder.getStatus() != null) WHERE("status = #{status}");
            if (parkOrder.getDelFlag() != null) WHERE("del_flag = #{delFlag}");
        }}.toString();
    }
}