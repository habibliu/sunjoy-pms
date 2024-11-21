package com.sunjoy.parkctrl.mapper.provider;

import com.sunjoy.parking.entity.PmsParkTransaction;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public class ParkTransactionSqlProvider {
    public String update(final PmsParkTransaction transaction) {
        return new SQL() {{
            UPDATE("pms_park_transaction");
            if (transaction.getTenantId() != null) SET("tenant_id = #{tenantId}");
            if (transaction.getOpuId() != null) SET("opu_id = #{opuId}");
            if (transaction.getParkId() != null) SET("park_id = #{parkId}");
            if (transaction.getParkName() != null) SET("park_name = #{parkName}");
            if (transaction.getVehicleId() != null) SET("vehicle_id = #{vehicleId}");
            if (transaction.getLicensePlate() != null) SET("license_plate = #{licensePlate}");
            if (transaction.getEntryServiceId() != null) SET("entry_service_id = #{entryServiceId}");
            if (transaction.getEntryLaneId() != null) SET("entry_lane_id = #{entryLaneId}");
            if (transaction.getEntryLaneName() != null) SET("entry_lane_name = #{entryLaneName}");
            if (transaction.getEntryDeviceId() != null) SET("entry_device_id = #{entryDeviceId}");
            if (transaction.getEntryTime() != null) SET("entry_time = #{entryTime}");
            if (transaction.getEntryRelTime() != null) SET("entry_rel_time = #{entryRelTime}");
            if (transaction.getEntryRelMode() != null) SET("entry_rel_mode = #{entryRelMode}");
            if (transaction.getLotNo() != null) SET("lot_no = #{lotNo}");
            if (transaction.getExitServiceId() != null) SET("exit_service_id = #{exitServiceId}");
            if (transaction.getExitLaneId() != null) SET("exit_lane_id = #{exitLaneId}");
            if (transaction.getExitLaneName() != null) SET("exit_lane_name = #{exitLaneName}");
            if (transaction.getExitDeviceId() != null) SET("exit_device_id = #{exitDeviceId}");
            if (transaction.getExitTime() != null) SET("exit_time = #{exitTime}");
            if (transaction.getExitRelTime() != null) SET("exit_rel_time = #{exitRelTime}");
            if (transaction.getExitRelMode() != null) SET("exit_rel_mode = #{exitRelMode}");
            if (transaction.getParkingDuration() != null) SET("parking_duration = #{parkingDuration}");
            if (transaction.getStatus() != null) SET("status = #{status}");
            if (transaction.getDelFlag() != null) SET("del_flag = #{delFlag}");
            if (transaction.getUpdateBy() != null) SET("update_by = #{updateBy}");
            if (transaction.getUpdateTime() != null) SET("update_time = #{updateTime}");
            if (transaction.getRemark() != null) SET("remark = #{remark}");
            WHERE("trans_id = #{transId}");
        }}.toString();
    }

    public String findByCondition(final PmsParkTransaction condition) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_park_transaction");
            if (condition.getTenantId() != null) WHERE("tenant_id = #{tenantId}");
            if (condition.getOpuId() != null) WHERE("opu_id = #{opuId}");
            if (condition.getParkId() != null) WHERE("park_id = #{parkId}");
            if (condition.getEntryRefId() != null) WHERE("entry_ref_id = #{entryRefId}");
            if (condition.getEntryServiceId() != null) WHERE("entry_service_id = #{entryServiceId}");
            if (condition.getExitRefId() != null) WHERE("exit_ref_id = #{exitRefId}");
            if (condition.getExitServiceId() != null) WHERE("exit_service_id = #{exitServiceId}");
            if (condition.getLicensePlate() != null) WHERE("license_plate LIKE CONCAT('%', #{licensePlate}, '%')");
            if (condition.getStatus() != null) WHERE("status = #{status}");
            // 可以根据需要添加更多条件
        }}.toString();
    }
}