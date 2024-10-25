package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.parkmodel.entity.PmsDevice;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
public class PmsDeviceSqlProvider {
    public String selectPmsDevicesByCondition(final PmsDevice condition) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_device ");

            if (condition.getDeviceName() != null && !condition.getDeviceName().isEmpty()) {
                WHERE("device_name LIKE CONCAT('%', #{deviceName}, '%')\"");
            }
            if (condition.getOpuId() != null) {
                WHERE("opu_id = #{opuId}");
            }
            if (condition.getDeviceModel() != null) {
                WHERE("device_model LIKE CONCAT('%', #{deviceModel}, '%')");
            }
            if (condition.getStatus() != null) {
                WHERE("status = #{status}");
            }
            if (condition.getVendor() != null) {
                WHERE("vendor = #{vendor}");
            }

        }}.toString();
    }
}