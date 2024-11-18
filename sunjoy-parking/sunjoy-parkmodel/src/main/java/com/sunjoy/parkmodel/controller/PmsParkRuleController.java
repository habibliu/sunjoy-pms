package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parkmodel.service.IPmsParkRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/5
 */
@Slf4j
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

    @RequiresPermissions("parking:rule:add")
    @Log(title = "车场通行规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmsParkRule rule) {

        pmsParkRuleService.create(rule);
        log.info("通行规则新增成功，编号为{}", rule.toString());
        return toAjax(1);
    }

    /**
     * 获取规则详细信息
     *
     * @param rulId
     * @return
     */
    @RequiresPermissions("parking:device:list")
    @GetMapping("/{ruleId}")
    public AjaxResult getRuleInfo(@PathVariable(value = "ruleId", required = true) Long ruleId) {

        AjaxResult ajax = AjaxResult.success();

        ajax.put(AjaxResult.DATA_TAG, this.pmsParkRuleService.getParkRule(ruleId));
        return ajax;
    }

    @RequiresPermissions("parking:rule:update")
    @Log(title = "车场通行规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody PmsParkRule rule) {

        pmsParkRuleService.update(rule);
        log.info("通行规则更新成功，编号为{}", rule.toString());
        return toAjax(1);
    }
}