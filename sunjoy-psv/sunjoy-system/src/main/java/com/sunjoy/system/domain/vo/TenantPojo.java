package com.sunjoy.system.domain.vo;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.system.domain.SysTenant;
import lombok.Data;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/1
 */
@Data
public class TenantPojo extends SysTenant {
    /**
     * 菜单ID列表
     */
    private List<Long> menuList;

    public SysTenant getSysTenant() {
        SysTenant sysTenant = new SysTenant();
        BeanUtils.copyBeanProp(sysTenant, this);
        return sysTenant;
    }
}