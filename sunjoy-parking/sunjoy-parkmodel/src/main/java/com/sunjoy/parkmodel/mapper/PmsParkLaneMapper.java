package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parkmodel.entity.PmsParkLane;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@Mapper
public interface PmsParkLaneMapper {
    @Insert("INSERT INTO pms_park_lane (lane_id, park_id, direction, status, del_flag, create_by, create_time,  remark) " +
            "VALUES (#{laneId}, #{parkId}, #{direction}, #{status}, #{delFlag}, #{createBy}, #{createTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPmsParkLane(PmsParkLane pmsParkLane);

    @Select("SELECT * FROM pms_park_lane WHERE id = #{id}")
    PmsParkLane selectPmsParkLaneById(Long id);

    @Update("""
                <script>
                UPDATE pms_park_lane
                <set>
                    <if test="laneId != null">lane_id = #{laneId},</if>
                    <if test="parkId != null">park_id = #{parkId},</if>
                    <if test="direction != null and direction != ''">direction = #{direction},</if>
                    <if test="status != null and status != ''">status = #{status},</if>
                    <if test="delFlag != null and delFlag != ''">del_flag = #{delFlag},</if>
                    <if test="updateBy != null and updateBy != ''">update_by = #{updateBy},</if>
                    <if test="updateTime != null">update_time = #{updateTime},</if>
                    <if test="remark != null and remark != ''">remark = #{remark},</if>
                </set>
                WHERE id = #{id}
                </script>
            """)
    void updatePmsParkLane(PmsParkLane pmsParkLane);

    @Delete("DELETE FROM pms_park_lane WHERE id = #{id}")
    void deletePmsParkLane(@Param("id") Long id);

    @Select("SELECT * FROM pms_park_lane where park_id= #{parkId}")
    List<PmsParkLane> selectParkLanesByParkId(Long parkId);

    @Delete("DELETE FROM pms_park_lane WHERE park_id=#{parkId} and lane_id=#{laneId}")
    void deleteParkLaneByParkIdAndLaneId(@Param("parkId") Long parkId, @Param("laneId") Long laneId);

    @Select("""
                <script>
                    select * from pms_park_lane 
                    WHERE lane_id IN
                        <foreach item='id'  collection='laneIds' open='(' separator=',' close=')'>
                            #{id}
                        </foreach>
                </script>
            """)
    List<PmsParkLane> selectParkLanesByLaneIds(@Param("laneIds") List<Long> laneIds);
}