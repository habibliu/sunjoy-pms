package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parkmodel.mapper.provider.PmsVehicleServiceSqlProvider;
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
    @Insert("INSERT INTO pms_vehicle_service (service_id, service_name, tenant_id, opu_id, park_id, vehicle_id, " +
            "license_plate,lot_nos, start_date, end_date, owner_name, owner_phone, status, del_flag, " +
            "create_by, create_time, update_by, update_time, remark) " +
            "VALUES (#{serviceId},#{serviceName}, #{tenantId}, #{opuId}, #{parkId}, #{vehicleId}, " +
            "#{licensePlate},#{lotNos}, #{startDate}, #{endDate}, #{ownerName}, #{ownerPhone}, " +
            "#{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PmsVehicleService vehicleService);

    @Select("SELECT * FROM pms_vehicle_service WHERE id = #{id}")
    PmsVehicleService selectById(Long id);

    @UpdateProvider(type = PmsVehicleServiceSqlProvider.class, method = "updatePmsVehicleService")
    void update(PmsVehicleService vehicleService);

    @Delete("DELETE FROM pms_vehicle_service WHERE id = #{id}")
    void delete(Long id);

    @Select("SELECT * FROM pms_vehicle_service where del_flag='0' and status!='2'")
    List<PmsVehicleService> selectAll();

    @SelectProvider(type = PmsVehicleServiceSqlProvider.class, method = "selectByVehicleIds")
    List<PmsVehicleService> selectByVehicleIds(List<Long> vehicleIds);

    @Select("SELECT * FROM pms_vehicle_service where vehicle_id=#{vehicleId} and del_flag='0'")
    List<PmsVehicleService> selectByVehicleId(Long vehicleId);
}