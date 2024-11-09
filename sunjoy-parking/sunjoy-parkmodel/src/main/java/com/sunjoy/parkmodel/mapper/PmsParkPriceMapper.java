package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parkmodel.mapper.provider.PmsParkPriceSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/6
 */
@Mapper
public interface PmsParkPriceMapper {
    @Insert("INSERT INTO pms_park_price (tenant_id, opu_id, price_name, free, free_duration, " +
            "uniform_price, price, price_unit, price_quantity, max_fee, max_unit, max_quantity, " +
            "status, del_flag, create_by, create_time, update_by, update_time, remark) " +
            "VALUES (#{tenantId}, #{opuId}, #{priceName}, #{free}, #{freeDuration}, " +
            "#{uniformPrice}, #{price}, #{priceUnit}, #{priceQuantity}, #{maxFee}, " +
            "#{maxUnit}, #{maxQuantity}, #{status}, #{delFlag}, #{createBy}, #{createTime}, " +
            "#{updateBy}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "priceId")
    void insert(PmsParkPrice parkPrice);

    @Select("SELECT * FROM pms_park_price WHERE price_id = #{priceId}")
    PmsParkPrice selectById(Long priceId);

    @Update("UPDATE pms_park_price SET tenant_id = #{tenantId}, opu_id = #{opuId}, " +
            "price_name = #{priceName}, free = #{free}, free_duration = #{freeDuration}, " +
            "uniform_price = #{uniformPrice}, price = #{price}, price_unit = #{priceUnit}, " +
            "price_quantity = #{priceQuantity}, max_fee = #{maxFee}, max_unit = #{maxUnit}, " +
            "max_quantity = #{maxQuantity}, status = #{status}, del_flag = #{delFlag}, " +
            "create_by = #{createBy}, create_time = #{createTime}, update_by = #{updateBy}, " +
            "update_time = #{updateTime}, remark = #{remark} " +
            "WHERE price_id = #{priceId}")
    void update(PmsParkPrice parkPrice);

    @Delete("DELETE FROM pms_park_price WHERE price_id = #{priceId}")
    void delete(Long priceId);

    @SelectProvider(type = PmsParkPriceSqlProvider.class, method = "selectByCondition")
    List<PmsParkPrice> selectByCondition(PmsParkPrice parkPrice);
}