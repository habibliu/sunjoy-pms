package com.sunjoy.parkctrl.mapper;

import com.sunjoy.parkctrl.mapper.provider.ParkPaymentProvider;
import com.sunjoy.system.api.domain.PmsParkPayment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
@Mapper
public interface PmsParkPaymentMapper {
    /**
     * 插入支付记录
     */
    @Insert("INSERT INTO pms_park_payment(order_id, trans_id, tenant_id, opu_id, park_id, license_plate, " +
            "service_id, order_amount, payment_amount, discount_amount, payment_methods, payment_channel, " +
            "payment_time, transaction_id, status, del_flag, create_by, create_time, " +
            "update_by, update_time, remark) " +
            "VALUES(#{orderId}, #{transId}, #{tenantId}, #{opuId}, #{parkId}, #{licensePlate}, " +
            "#{serviceId}, #{orderAmount}, #{paymentAmount}, #{discountAmount}, #{paymentMethods}, " +
            "#{paymentChannel},  #{paymentTime}, #{transactionId}, #{status}, #{delFlag}, " +
            "#{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    void insertPayment(PmsParkPayment payment);

    /**
     * 更新支付记录，只有那些不为空的字段会被更新
     */
    @UpdateProvider(type = ParkPaymentProvider.class, method = "updatePayment")
    void updatePayment(PmsParkPayment payment);

    /**
     * 根据条件查询支付记录，只有那些不为空的字段会被作为查询条件
     */
    @SelectProvider(type = ParkPaymentProvider.class, method = "selectPayment")
    List<PmsParkPayment> selectPayment(PmsParkPayment payment);
}