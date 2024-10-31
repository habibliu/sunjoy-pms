package com.sunjoy.system.service.impl;


import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.system.domain.SysRegion;
import com.sunjoy.system.domain.vo.RegionTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形结构构建类
 *
 * @author Habib
 * @date 2024/10/30
 */
public class RegionTreeBuilder {

    public List<RegionTreeNode> buildTree(List<SysRegion> regions) {
        // 创建一个地图来存储每个区域
        Map<String, RegionTreeNode> regionMap = new HashMap<>();
        List<RegionTreeNode> nodeList = new ArrayList<>();
        for (SysRegion region : regions) {
            //生成一个treeNode
            RegionTreeNode node = new RegionTreeNode();
            BeanUtils.copyBeanProp(node, region);
            // 初始化子节点列表
            node.setChildren(new ArrayList<>());
            nodeList.add(node);
            regionMap.put(node.getRegionId(), node);
        }


        // 构建树形结构
        List<RegionTreeNode> tree = new ArrayList<>();
        for (RegionTreeNode region : nodeList) {

            if (region.getParentId() == null || region.getParentId().isEmpty()) {
                // 如果没有父区域，则为根节点
                tree.add(region);
            } else {
                // 有父区域，则添加到父节点的子节点列表中
                RegionTreeNode parent = regionMap.get(region.getParentId());
                if (parent != null) {

                    parent.getChildren().add(region);
                }
            }
        }
        return tree;
    }
}