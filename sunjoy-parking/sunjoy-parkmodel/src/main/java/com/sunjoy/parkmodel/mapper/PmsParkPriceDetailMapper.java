package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsParkPriceDetail;
import com.sunjoy.parkmodel.mapper.provider.PmsParkPriceDetailSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/7
 */
@Mapper
public interface PmsParkPriceDetailMapper {
    /**
     * 插入新的收费价目明细
     */
    @Insert("INSERT INTO pms_park_price_detail(price_id, time_start, time_end, price, price_unit, price_quantity, status, del_flag, create_by, create_time, update_by, update_time, remark) " +
            "VALUES(#{priceId}, #{timeStart}, #{timeEnd}, #{price}, #{priceUnit}, #{priceQuantity}, #{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PmsParkPriceDetail parkPriceDetail);

    /**
     * 根据 ID 删除收费价目明细
     */
    @Delete("DELETE FROM pms_park_price_detail WHERE id = #{id} AND del_flag = '0'")
    int deleteById(Long id);

    /**
     * 更新收费价目明细
     */
    @Update("UPDATE pms_park_price_detail SET price_id = #{priceId}, time_start = #{timeStart}, time_end = #{timeEnd}, price = #{price}, " +
            "price_unit = #{priceUnit}, price_quantity = #{priceQuantity}, status = #{status}, del_flag = #{delFlag}, " +
            "create_by = #{createBy}, create_time = #{createTime}, update_by = #{updateBy}, update_time = #{updateTime}, remark = #{remark} " +
            "WHERE id = #{id}")
    int update(PmsParkPriceDetail parkPriceDetail);

    /**
     * 根据 ID 查询收费价目明细
     */
    @Select("SELECT * FROM pms_park_price_detail WHERE id = #{id} AND del_flag = '0'")
    PmsParkPriceDetail selectById(Long id);

    /**
     * 查询所有未删除的收费价目明细
     */
    @SelectProvider(type = PmsParkPriceDetailSqlProvider.class, method = "selectByConditions")
    List<PmsParkPriceDetail> selectByConditions(PmsParkPriceDetail parkPriceDetail);
}