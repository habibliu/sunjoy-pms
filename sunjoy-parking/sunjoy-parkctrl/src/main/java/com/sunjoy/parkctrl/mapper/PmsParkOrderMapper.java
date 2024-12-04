package com.sunjoy.parkctrl.mapper;

import com.sunjoy.parkctrl.mapper.provider.ParkOrderProvider;
import com.sunjoy.parking.entity.PmsParkOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
@Mapper
public interface PmsParkOrderMapper {
    /**
     * 插入订单
     */
    @Insert("INSERT INTO pms_park_order(order_type, trans_id, tenant_id, opu_id, park_id, park_name, " +
            "vehicle_id, license_plate, service_id, service_name, start_time, end_time, billing_amount, " +
            "real_amount, free_amount, order_detail, status, del_flag, create_by, create_time, " +
            "update_by, update_time, remark) " +
            "VALUES(#{orderType}, #{transId}, #{tenantId}, #{opuId}, #{parkId}, #{parkName}, " +
            "#{vehicleId}, #{licensePlate}, #{serviceId}, #{serviceName}, #{startTime}, " +
            "#{endTime}, #{billingAmount}, #{realAmount}, #{freeAmount}, #{orderDetail}, " +
            "#{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    void insertParkOrder(PmsParkOrder parkOrder);

    /**
     * 更新订单，只有那些不为空的字段会被更新
     */
    @UpdateProvider(type = ParkOrderProvider.class, method = "updateParkOrder")
    void updateParkOrder(PmsParkOrder parkOrder);

    /**
     * 根据条件查询订单，只有那些不为空的字段会被作为查询条件
     */
    @SelectProvider(type = ParkOrderProvider.class, method = "selectParkOrder")
    List<PmsParkOrder> selectParkOrder(PmsParkOrder parkOrder);

    @Select("SELECT order_id,order_type, trans_id, tenant_id, opu_id, park_id, park_name," +
            "vehicle_id, license_plate, service_id, service_name, start_time, end_time, billing_amount, " +
            "real_amount, free_amount,  status, del_flag, create_by, create_time," +
            "update_by, update_time FROM pms_park_order where order_id=#{orderId}")
    PmsParkOrder selectParkOrderById(Long orderId);
}