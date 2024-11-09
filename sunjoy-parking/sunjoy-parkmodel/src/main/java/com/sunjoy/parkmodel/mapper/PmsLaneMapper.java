package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsLane;
import com.sunjoy.parkmodel.mapper.provider.PmsLaneSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 通道数据库接口文件
 *
 * @author Habib
 * @date 2024/10/25
 */
@Mapper
public interface PmsLaneMapper {

    @Insert("INSERT INTO pms_lane (lane_name, tenant_id,opu_id, rap,link_outer, status, del_flag, create_by, create_time,  remark) VALUES (#{laneName}, #{tenantId},#{opuId}, #{rap}, #{linkOuter}, #{status}, #{delFlag}, #{createBy}, #{createTime},  #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "laneId")
    int insertPmsLane(PmsLane pmsLane);

    @Select("SELECT * FROM pms_lane WHERE lane_id = #{laneId}")
    PmsLane selectPmsLaneById(Long laneId);

    @UpdateProvider(type = PmsLaneSqlProvider.class, method = "updatePmsLane")
    int updatePmsLane(PmsLane pmsLane);

    @Delete("DELETE FROM pms_lane WHERE lane_id = #{laneId}")
    int deletePmsLane(Long laneId);

    //批量删除，此处的语法要用到jdk13以上的语言版本
    @Delete("""
                 <script>
                DELETE FROM pms_lane
                WHERE lane_id IN
                <foreach item='id' collection='laneIds' open='(' separator=',' close=')'>
                    #{id}
                </foreach>
                </script>
            """)
    int deletePmsLanes(@Param("laneIds") List<Long> laneIds);

    @SelectProvider(type = PmsLaneSqlProvider.class, method = "selectPmsLanesByCondition")
    List<PmsLane> selectPmsLanesByCondition(PmsLane pmsLane);

    @Select("""
              <script>
                SELECT * FROM pms_lane
                WHERE lane_id IN
                <foreach item='id'  collection='laneIds' open='(' separator=',' close=')'>
                    #{id}
                </foreach>
              </script>
            """)
    List<PmsLane> selectPmsLanes(@Param("laneIds") List<Long> laneIds);
}