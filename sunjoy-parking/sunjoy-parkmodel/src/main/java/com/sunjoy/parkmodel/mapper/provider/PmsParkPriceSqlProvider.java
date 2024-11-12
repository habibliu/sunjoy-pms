package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/6
 */
public class PmsParkPriceSqlProvider {
    public String selectByCondition(final PmsParkPrice condition) {
        return new SQL() {{
            SELECT("l.*");
            FROM("pms_park_price l");

            if (condition.getPriceName() != null && !condition.getPriceName().isEmpty()) {
                WHERE(".price_name = #{priceName}");
            }
            if (condition.getTenantId() != null) {
                WHERE("l.tenant_id = #{tenantId}");
            }
            if (condition.getOpuId() != null) {
                List<Long> opuIds = SecurityUtils.getAuthDeptIds(SecurityUtils.getUserId(), condition.getOpuId());
                String inClause = opuIds.stream()
                        .map(String::valueOf) // 将 Long 转换为 String
                        .collect(Collectors.joining(", ")); // 连接成字符串
                WHERE("l.opu_id IN (" + inClause + ")");
            }
            if (condition.getUniformPrice() != null) {
                WHERE("l.uniform_price = #{uniformPrice}");
            }
            if (condition.getStatus() != null) {
                WHERE("l.status = #{status}");
            }
            if (condition.getFree() != null) {
                WHERE("l.free = #{free}");
            }


        }}.toString();
    }

    public String updateParkPrice(PmsParkPrice parkPrice) {
        return new SQL() {{
            UPDATE("pms_park_price");

            if (parkPrice.getTenantId() != null) {
                SET("tenant_id = #{tenantId}");
            }
            if (parkPrice.getOpuId() != null) {
                SET("opu_id = #{opuId}");
            }
            if (parkPrice.getPriceName() != null) {
                SET("price_name = #{priceName}");
            }
            if (parkPrice.getFree() != null) {
                SET("free = #{free}");
            }
            if (parkPrice.getFreeDuration() != null) {
                SET("free_duration = #{freeDuration}");
            }
            if (parkPrice.getUniformPrice() != null) {
                SET("uniform_price = #{uniformPrice}");
            }
            if (parkPrice.getPrice() != null) {
                SET("price = #{price}");
            }
            if (parkPrice.getPriceUnit() != null) {
                SET("price_unit = #{priceUnit}");
            }
            if (parkPrice.getPriceQuantity() != null) {
                SET("price_quantity = #{priceQuantity}");
            }
            if (parkPrice.getMaxFee() != null) {
                SET("max_fee = #{maxFee}");
            }
            if (parkPrice.getMaxUnit() != null) {
                SET("max_unit = #{maxUnit}");
            }
            if (parkPrice.getMaxQuantity() != null) {
                SET("max_quantity = #{maxQuantity}");
            }
            if (parkPrice.getStatus() != null) {
                SET("status = #{status}");
            }
            if (parkPrice.getDelFlag() != null) {
                SET("del_flag = #{delFlag}");
            }
            if (parkPrice.getCreateBy() != null) {
                SET("create_by = #{createBy}");
            }
            if (parkPrice.getCreateTime() != null) {
                SET("create_time = #{createTime}");
            }
            if (parkPrice.getUpdateBy() != null) {
                SET("update_by = #{updateBy}");
            }
            if (parkPrice.getUpdateTime() != null) {
                SET("update_time = #{updateTime}");
            }
            if (parkPrice.getRemark() != null) {
                SET("remark = #{remark}");
            }

            WHERE("price_id = #{priceId}");
        }}.toString();
    }
}