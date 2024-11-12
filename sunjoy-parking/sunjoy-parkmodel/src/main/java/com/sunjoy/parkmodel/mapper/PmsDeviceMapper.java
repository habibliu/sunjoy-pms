package com.sunjoy.parkmodel.mapper;


import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parkmodel.mapper.provider.PmsDeviceSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@Mapper
public interface PmsDeviceMapper {
    @Insert("INSERT INTO pms_device (device_name, device_model,tenant_id, opu_id, device_code,functions, vendor, producer, " +
            "params_parse, status, del_flag, create_by, create_time,  remark) " +
            "VALUES (#{deviceName}, #{deviceModel},#{tenantId}, #{opuId},#{deviceCode}, #{functions}, #{vendor}, #{producer}, " +
            "#{paramsParse}, #{status}, #{delFlag}, #{createBy}, #{createTime},#{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "deviceId")
    void insert(PmsDevice device);

    @Select("SELECT * FROM pms_device WHERE device_id = #{deviceId}")
    PmsDevice selectById(Long deviceId);

    @SelectProvider(type = PmsDeviceSqlProvider.class, method = "selectPmsDevicesByCondition")
    List<PmsDevice> selectPmsDevicesByCondition(PmsDevice device);

    @Update("UPDATE pms_device SET device_name = #{deviceName}, device_model = #{deviceModel}, device_code = #{deviceCode}, opu_id = #{opuId}, " +
            "functions = #{functions}, vendor = #{vendor}, producer = #{producer}, params_parse = #{paramsParse}, " +
            "status = #{status}, del_flag = #{delFlag}, " +
            "update_by = #{updateBy}, update_time = #{updateTime}, remark = #{remark} " +
            "WHERE device_id = #{deviceId}")
    void update(PmsDevice device);

    @Delete("DELETE FROM pms_device WHERE device_id = #{deviceId}")
    void delete(Long deviceId);

    @Select("""
                    <script>
                    SELECT * FROM pms_device WHERE device_id IN 
                                    
                            <foreach item='id'  collection='deviceIds' open='(' separator=',' close=')'>
                            #{id}
                        </foreach>
                                 </script>
            """)
    List<PmsDevice> selectByIdS(@Param("deviceIds") List<Long> deviceIds);
}