package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parkmodel.service.IPmsParkRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/5
 */
@RestController
@RequestMapping("/rule")
public class PmsParkRuleController extends BaseController {
    @Autowired
    private IPmsParkRuleService pmsParkRuleService;

    @RequiresPermissions("parking:rule:list")
    @GetMapping("/list")
    public TableDataInfo list(PmsParkRule rule) {
        startPage();
        List<PmsParkRule> list = pmsParkRuleService.getParkRules(rule);
        return getDataTable(list);
    }
}