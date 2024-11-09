package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsVehicleService;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/6
 */
@Mapper
public interface PmsVehicleServiceMapper {
    @Insert("INSERT INTO pms_vehicle_service (service_id, tenant_id, opu_id, park_id, vehicle_id, " +
            "license_plate, start_date, end_date, owner_name, owner_phone, status, del_flag, " +
            "create_by, create_time, update_by, update_time, remark) " +
            "VALUES (#{serviceId}, #{tenantId}, #{opuId}, #{parkId}, #{vehicleId}, " +
            "#{licensePlate}, #{startDate}, #{endDate}, #{ownerName}, #{ownerPhone}, " +
            "#{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PmsVehicleService vehicleService);

    @Select("SELECT * FROM pms_vehicle_service WHERE id = #{id}")
    PmsVehicleService selectById(Long id);

    @Update("UPDATE pms_vehicle_service SET service_id = #{serviceId}, tenant_id = #{tenantId}, " +
            "opu_id = #{opuId}, park_id = #{parkId}, vehicle_id = #{vehicleId}, " +
            "license_plate = #{licensePlate}, start_date = #{startDate}, end_date = #{endDate}, " +
            "owner_name = #{ownerName}, owner_phone = #{ownerPhone}, status = #{status}, " +
            "del_flag = #{delFlag}, create_by = #{createBy}, create_time = #{createTime}, " +
            "update_by = #{updateBy}, update_time = #{updateTime}, remark = #{remark} " +
            "WHERE id = #{id}")
    void update(PmsVehicleService vehicleService);

    @Delete("DELETE FROM pms_vehicle_service WHERE id = #{id}")
    void delete(Long id);

    @Select("SELECT * FROM pms_vehicle_service where del_flag='0' and status!='2'")
    List<PmsVehicleService> selectAll();
}