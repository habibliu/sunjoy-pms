package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parkmodel.mapper.provider.PmsVehicleSqlProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

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
    void insert(PmsVehicle vehicle);

    @UpdateProvider(type = PmsVehicleSqlProvider.class, method = "updatePmsVehicle")
    void update(PmsVehicle vehicle);

    @SelectProvider(type = PmsVehicleSqlProvider.class, method = "selectPmsVehicle")
    List<PmsVehicle> select(PmsVehicle vehicle);
}