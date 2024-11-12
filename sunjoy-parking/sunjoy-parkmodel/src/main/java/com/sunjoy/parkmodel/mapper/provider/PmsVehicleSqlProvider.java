package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.parking.entity.PmsVehicle;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/11
 */
public class PmsVehicleSqlProvider {
    public String updatePmsVehicle(PmsVehicle vehicle) {
        return new SQL() {{
            UPDATE("pms_vehicle");

            if (vehicle.getTenantId() != null) {
                SET("tenant_id = #{tenantId}");
            }
            if (vehicle.getOpuId() != null) {
                SET("opu_id = #{opuId}");
            }
            if (vehicle.getLicensePlate() != null) {
                SET("license_plate = #{licensePlate}");
            }
            if (vehicle.getBrand() != null) {
                SET("brand = #{brand}");
            }
            if (vehicle.getModel() != null) {
                SET("model = #{model}");
            }
            if (vehicle.getVehicleType() != null) {
                SET("vehicle_type = #{vehicleType}");
            }
            if (vehicle.getOwnerName() != null) {
                SET("owner_name = #{ownerName}");
            }
            if (vehicle.getOwnerPhone() != null) {
                SET("owner_phone = #{ownerPhone}");
            }
            if (vehicle.getOwnerAddr() != null) {
                SET("owner_addr = #{ownerAddr}");
            }
            if (vehicle.getRegistDate() != null) {
                SET("regist_date = #{registDate}");
            }
            if (vehicle.getStatus() != null) {
                SET("status = #{status}");
            }
            if (vehicle.getDelFlag() != null) {
                SET("del_flag = #{delFlag}");
            }
            if (vehicle.getCreateBy() != null) {
                SET("create_by = #{createBy}");
            }
            if (vehicle.getCreateTime() != null) {
                SET("create_time = #{createTime}");
            }
            if (vehicle.getUpdateBy() != null) {
                SET("update_by = #{updateBy}");
            }
            if (vehicle.getUpdateTime() != null) {
                SET("update_time = #{updateTime}");
            }
            if (vehicle.getRemark() != null) {
                SET("remark = #{remark}");
            }

            WHERE("vehicle_id = #{vehicleId}");
        }}.toString();
    }

    public String selectPmsVehicle(PmsVehicle vehicle) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_vehicle");

            if (vehicle.getVehicleId() != null) {
                WHERE("vehicle_id = #{vehicleId}");
            }
            if (vehicle.getTenantId() != null) {
                WHERE("tenant_id = #{tenantId}");
            }
            if (vehicle.getOpuId() != null) {
                WHERE("opu_id = #{opuId}");
            }
            if (vehicle.getLicensePlate() != null) {
                WHERE("license_plate = #{licensePlate}");
            }
            if (vehicle.getStatus() != null) {
                WHERE("status = #{status}");
            }
            // 添加其他条件可以继续扩展
        }}.toString();
    }
}