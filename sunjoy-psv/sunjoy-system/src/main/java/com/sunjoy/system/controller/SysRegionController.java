package com.sunjoy.system.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.system.domain.SysRegion;
import com.sunjoy.system.service.ISysRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/31
 */
@Slf4j
@RestController
@RequestMapping("/region")
public class SysRegionController extends BaseController {
    @Autowired
    private ISysRegionService regionService;


    /**
     * 获取租户列表列表
     */

    @GetMapping("/list")
    public AjaxResult list(SysRegion region) {
        String parentId = null;
        if (region != null && region.getParentId() != null) {
            parentId = region.getParentId();
        }
        List<SysRegion> result = this.regionService.getRegionsByParentId(parentId);

        log.info("成功返回区域记录数:{}", result.size());
        return success(result);
    }
}