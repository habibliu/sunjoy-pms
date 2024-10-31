package com.sunjoy.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 区域树节点
 *
 * @author Habib
 * @date 2024/10/30
 */
@Data
public class RegionTreeNode {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 区域id
     */
    private String regionId;

    /**
     * 父区域id
     */
    private String parentId;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 子区域列表
     */
    private List<RegionTreeNode> children; // 新增属性
}