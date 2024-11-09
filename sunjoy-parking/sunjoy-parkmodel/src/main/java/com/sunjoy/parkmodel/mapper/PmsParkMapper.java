package com.sunjoy.parkmodel.mapper;


import com.sunjoy.parking.entity.PmsPark;
import com.sunjoy.parkmodel.mapper.provider.PmsParkSqlProvider;
import com.sunjoy.parkmodel.pojo.ParkPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

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

    @Options(useGeneratedKeys = true, keyProperty = "parkId") // 使用自增长主键
    public int insertPark(PmsPark pmsPark);

    public int updatePark(PmsPark pmsPark);

    /**
     * 根据主键查询车场档案信息
     *
     * @param parkId
     * @return
     */
    public PmsPark selectParkById(Long parkId);
}