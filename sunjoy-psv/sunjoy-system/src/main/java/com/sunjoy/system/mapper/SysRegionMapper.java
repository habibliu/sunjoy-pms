package com.sunjoy.system.mapper;

import com.sunjoy.system.domain.SysRegion;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/30
 */
@Mapper
public interface SysRegionMapper {

    /**
     * 根据条件查询区域
     *
     * @param region 区域查询条件
     * @return 区域列表
     */
    @Select({
            "<script>",
            "SELECT * FROM sys_region",
            "WHERE 1 = 1",
            "<if test='regionId != null and regionId != \"\"'>",
            "AND region_id = #{regionId}",
            "</if>",
            "<if test='parentId != null and parentId != \"\"'>",
            "AND parent_id = #{parentId}",
            "</if>",
            "<if test='status != null'>",
            "AND status = #{status}",
            "</if>",
            "<if test='delFlag != null'>",
            "AND del_flag = #{delFlag}",
            "</if>",
            "</script>"
    })
    List<SysRegion> selectByConditions(SysRegion region);

    @Select("SELECT region_id,region_name,parent_id FROM sys_region")
    List<SysRegion> selectAll();

    /**
     * 插入新的区域
     *
     * @param region 区域对象
     * @return 插入的行数
     */
    @Insert("INSERT INTO sys_region (region_id, parent_id, ancestors, region_name, status, del_flag, create_by, create_time, update_by, update_time) " +
            "VALUES (#{regionId}, #{parentId}, #{ancestors}, #{regionName}, #{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime})")
    int insert(SysRegion region);

    /**
     * 更新区域信息
     *
     * @param region 区域对象
     * @return 更新的行数
     */
    @Update("UPDATE sys_region SET region_name = #{regionName}, status = #{status}, del_flag = #{delFlag}, " +
            "update_by = #{updateBy}, update_time = #{updateTime} WHERE id = #{id}")
    int update(SysRegion region);

    /**
     * 根据ID删除区域
     *
     * @param id 区域ID
     * @return 删除的行数
     */
    @Delete("DELETE FROM sys_region WHERE id = #{id}")
    int deleteById(Integer id);

    @Select({
            "<script>" +
            "SELECT region_id, region_name, parent_id FROM sys_region" +
            "<where>" +
            "<choose>" +
            "<when test='parentId != null' >" +
            "           AND parent_id= #{parentId}" +
            "</when>" +

            "<otherwise>" +
            "AND parent_id is null" +
            "                 </otherwise>" +
            "</choose>" +
            "</where>" +
            "</script>"
    })
    List<SysRegion> selectRegionsByParentId(String parentId);
}