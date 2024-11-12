package com.sunjoy.parkmodel.mapper;


import com.sunjoy.parking.entity.PmsPark;
import com.sunjoy.parkmodel.mapper.provider.PmsParkSqlProvider;
import com.sunjoy.parkmodel.pojo.ParkPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Habib
 * @since 2024-10-18
 */
@Mapper
public interface PmsParkMapper {

    /**
     * 查询
     *
     * @param condition
     * @return
     */
    @SelectProvider(type = PmsParkSqlProvider.class, method = "selectByCondition")
    public List<PmsPark> selectParkList(ParkPojo condition);

    /**
     * 使用自增长主键
     *
     * @param pmsPark
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "parkId")
    @Insert("INSERT INTO pms_park (park_name, parent_id, park_type, tenant_id, opu_id, opu_name, leader, phone, region, address, total_lots, order_num, status,del_flag,create_by,create_time) " +
            "VALUES (#{pmsPark.parkName}, #{pmsPark.parentId}, #{pmsPark.parkType}, #{pmsPark.tenantId}, #{pmsPark.opuId}, #{pmsPark.opuName}, #{pmsPark.leader}, #{pmsPark.phone}, #{pmsPark.region}, #{pmsPark.address}, #{pmsPark.totalLots}, #{pmsPark.orderNum}, #{pmsPark.status}, #{pmsPark.delFlag}, #{pmsPark.createBy}, #{pmsPark.createTime})")
    public int insertPark(@Param("pmsPark") PmsPark pmsPark);

    public int updatePark(PmsPark pmsPark);

    /**
     * 根据主键查询车场档案信息
     *
     * @param parkId
     * @return
     */
    public PmsPark selectParkById(Long parkId);
}