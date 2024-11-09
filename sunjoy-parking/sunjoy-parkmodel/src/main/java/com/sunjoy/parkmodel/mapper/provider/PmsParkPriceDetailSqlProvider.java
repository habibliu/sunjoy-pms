package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.parking.entity.PmsParkPriceDetail;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/7
 */
public class PmsParkPriceDetailSqlProvider {
    public String selectByConditions(PmsParkPriceDetail condition) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_park_price_detail");
            WHERE("del_flag = '0'"); // 只查询未删除记录

            // 可选条件
            if (condition.getId() != null) {
                WHERE("id = #{id}");
            }
            if (condition.getPriceId() != null) {
                WHERE("price_id = #{priceId}");
            }
            if (condition.getTimeStart() != null) {
                WHERE("time_start >= #{timeStart}");
            }
            if (condition.getTimeEnd() != null) {
                WHERE("time_end <= #{timeEnd}");
            }
            if (condition.getStatus() != null) {
                WHERE("status = #{status}");
            }
            // 可以根据需要添加更多条件
        }}.toString();
    }
}