package com.sunjoy.parkmodel.mapper;


import com.sunjoy.parking.entity.PmsLaneDevice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/29
 */
@Mapper
public interface PmsLaneDeviceMapper {


    @Insert("INSERT INTO pms_lane_device (device_id, lane_id, park_id, status, del_flag, create_by, create_time, update_by, update_time, remark) " +
            "VALUES (#{deviceId}, #{laneId}, #{parkId}, #{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PmsLaneDevice laneDevice);

    @Select("SELECT * FROM pms_lane_device WHERE id = #{id}")
    PmsLaneDevice selectById(Long id);

    @Update("UPDATE pms_lane_device SET device_id = #{deviceId}, lane_id = #{laneId}, park_id = #{parkId}, " +
            "status = #{status}, del_flag = #{delFlag}, create_by = #{createBy}, create_time = #{createTime}, " +
            "update_by = #{updateBy}, update_time = #{updateTime}, remark = #{remark} WHERE id = #{id}")
    int update(PmsLaneDevice laneDevice);

    @Delete("DELETE FROM pms_lane_device WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT * FROM pms_lane_device WHERE del_flag = '0'")
    List<PmsLaneDevice> selectAll();

    /**
     * 根据车场ID和通道ID删除通道设备关系
     *
     * @param parkId 车场ID
     * @param laneId 通道ID
     * @return
     */
    @Delete("DELETE FROM pms_lane_device WHERE park_id = #{parkId} and lane_id=#{laneId}")
    int deleteByParkIdAndLaneId(@Param("parkId") Long parkId, @Param("laneId") Long laneId);

    @Select("SELECT * FROM pms_lane_device WHERE park_id = #{parkId}  and del_flag = '0'")
    List<PmsLaneDevice> selectByParkId(@Param("parkId") Long parkId);

    @Delete("""
                <script>
                    DELETE FROM pms_lane_device WHERE id IN
                    <foreach item='id'  collection='ids' open='(' separator=',' close=')'>
                        #{id}
                    </foreach>
                                       
                </script>
            """)
    int deleteBatch(List<Long> ids);
}