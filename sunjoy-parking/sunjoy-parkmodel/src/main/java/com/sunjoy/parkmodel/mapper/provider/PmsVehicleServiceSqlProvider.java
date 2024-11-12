package com.sunjoy.parkmodel.mapper.provider;


import com.sunjoy.parking.entity.PmsVehicleService;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/11
 */
public class PmsVehicleServiceSqlProvider {

    public String updatePmsVehicleService(PmsVehicleService vehicleService) {
        return new SQL() {{
            UPDATE("pms_vehicle_service");

            if (vehicleService.getTenantId() != null) {
                SET("tenant_id = #{tenantId}");
            }
            if (vehicleService.getOpuId() != null) {
                SET("opu_id = #{opuId}");
            }
            if (vehicleService.getParkId() != null) {
                SET("park_id = #{parkId}");
            }
            if (vehicleService.getVehicleId() != null) {
                SET("vehicle_id = #{vehicleId}");
            }
            if (vehicleService.getLicensePlate() != null) {
                SET("license_plate = #{licensePlate}");
            }
            if (vehicleService.getStartDate() != null) {
                SET("start_date = #{startDate}");
            }
            if (vehicleService.getEndDate() != null) {
                SET("end_date = #{endDate}");
            }
            if (vehicleService.getOwnerName() != null) {
                SET("owner_name = #{ownerName}");
            }
            if (vehicleService.getOwnerPhone() != null) {
                SET("owner_phone = #{ownerPhone}");
            }
            if (vehicleService.getStatus() != null) {
                SET("status = #{status}");
            }
            if (vehicleService.getDelFlag() != null) {
                SET("del_flag = #{delFlag}");
            }
            if (vehicleService.getCreateBy() != null) {
                SET("create_by = #{createBy}");
            }
            if (vehicleService.getCreateTime() != null) {
                SET("create_time = #{createTime}");
            }
            if (vehicleService.getUpdateBy() != null) {
                SET("update_by = #{updateBy}");
            }
            if (vehicleService.getUpdateTime() != null) {
                SET("update_time = #{updateTime}");
            }
            if (vehicleService.getRemark() != null) {
                SET("remark = #{remark}");
            }

            WHERE("id = #{id}");
        }}.toString();
    }

    public String selectByVehicleIds(List<Long> vehicleIds) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_vehicle_service");
            if (vehicleIds != null && !vehicleIds.isEmpty()) {
                WHERE("vehicle_id IN (" +
                      String.join(",", vehicleIds.stream()
                              .map(id -> String.valueOf(id))
                              .toArray(String[]::new)) + ")");
            }
            
        }}.toString();
    }
}