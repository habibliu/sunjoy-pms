package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parkmodel.mapper.provider.PmsVehicleSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/11
 */
@Mapper
public interface PmsVehicleMapper {

    @Insert("INSERT INTO pms_vehicle (tenant_id, opu_id, license_plate, brand, model, vehicle_type, " +
            "owner_name, owner_phone, owner_addr, regist_date, status, del_flag, create_by, create_time, " +
            "update_by, update_time, remark) " +
            "VALUES (#{tenantId}, #{opuId}, #{licensePlate}, #{brand}, #{model}, #{vehicleType}, " +
            "#{ownerName}, #{ownerPhone}, #{ownerAddr}, #{registDate}, #{status}, #{delFlag}, " +
            "#{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "vehicleId")
        // 这里的 "id" 是对象的主键属性
    void insert(PmsVehicle vehicle);

    @UpdateProvider(type = PmsVehicleSqlProvider.class, method = "updatePmsVehicle")
    void update(PmsVehicle vehicle);

    @SelectProvider(type = PmsVehicleSqlProvider.class, method = "selectPmsVehicle")
    List<PmsVehicle> select(PmsVehicle vehicle);

    /**
     * 根据主键查询车辆档案信息
     *
     * @param vehicleId
     * @return
     */
    @Select("SELECT * FROM pms_vehicle WHERE vehicle_id = #{vehicleId}")
    PmsVehicle selectVehicleById(Long vehicleId);
}