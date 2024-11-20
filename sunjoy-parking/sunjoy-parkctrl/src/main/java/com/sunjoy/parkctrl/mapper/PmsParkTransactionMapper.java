package com.sunjoy.parkctrl.mapper;

import com.sunjoy.parkctrl.mapper.provider.ParkTransactionSqlProvider;
import com.sunjoy.parking.entity.PmsParkTransaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public interface PmsParkTransactionMapper {
    @Insert("INSERT INTO pms_park_transaction (tenant_id, opu_id, park_id, park_name, vehicle_id, " +
            "license_plate, entry_service_id, entry_lane_id, entry_lane_name, entry_device_id, " +
            "entry_time, entry_rel_time, entry_rel_mode, lot_no, exit_lane_id, exit_lane_name, " +
            "exit_device_id, exit_time, exit_rel_time, exit_rel_mode, parking_duration, status, " +
            "del_flag, create_by, create_time, update_by, update_time, remark) " +
            "VALUES (#{tenantId}, #{opuId}, #{parkId}, #{parkName}, #{vehicleId}, #{licensePlate}, " +
            "#{entryServiceId}, #{entryLaneId}, #{entryLaneName}, #{entryDeviceId}, #{entryTime}, " +
            "#{entryRelTime}, #{entryRelMode}, #{lotNo}, #{exitLaneId}, #{exitLaneName}, " +
            "#{exitDeviceId}, #{exitTime}, #{exitRelTime}, #{exitRelMode}, #{parkingDuration}, " +
            "#{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    void insert(PmsParkTransaction transaction);

    @Select("SELECT * FROM pms_park_transaction WHERE trans_id = #{transId}")
    PmsParkTransaction findById(Long transId);

    @SelectProvider(type = ParkTransactionSqlProvider.class, method = "findByCondition")
    List<PmsParkTransaction> findByCondition(PmsParkTransaction condition);

    @UpdateProvider(type = ParkTransactionSqlProvider.class, method = "update")
    void update(PmsParkTransaction transaction);

    @Delete("DELETE FROM pms_park_transaction WHERE trans_id = #{transId}")
    void delete(Long transId);
}