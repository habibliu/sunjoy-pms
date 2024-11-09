package com.sunjoy.system.service;

import com.sunjoy.system.domain.SysRegion;
import com.sunjoy.system.domain.vo.RegionTreeNode;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/30
 */
public interface ISysRegionService {

    final String KEY_REDIS_REGION = "sys:region:list";

    /**
     * 查询所有地区信息
     *
     * @return
     */
    List<SysRegion> getAllRegions();

    /**
     * 获取区域树状结构数据集
     *
     * @return
     */
    List<RegionTreeNode> selectRegionTreeList();

    /**
     * 根据父节点ID查询下辖地区列表
     *
     * @param parentId
     * @return
     */
    List<SysRegion> getRegionsByParentId(String parentId);


}