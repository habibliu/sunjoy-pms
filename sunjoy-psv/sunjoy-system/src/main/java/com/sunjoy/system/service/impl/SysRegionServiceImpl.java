package com.sunjoy.system.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.system.domain.SysRegion;
import com.sunjoy.system.domain.vo.RegionTreeNode;
import com.sunjoy.system.mapper.SysRegionMapper;
import com.sunjoy.system.service.ISysRegionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/30
 */
@Service
public class SysRegionServiceImpl implements ISysRegionService {

    @Autowired
    private SysRegionMapper sysRegionMapper;
    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void initCache() {
        if (!redisService.hasKey(ISysRegionService.KEY_REDIS_REGION)) {
            List<SysRegion> allRegions = this.getAllRegions();
            redisService.setCacheList(ISysRegionService.KEY_REDIS_REGION, allRegions);
        }
    }


    @Override
    public List<SysRegion> getAllRegions() {
        if (redisService.hasKey(ISysRegionService.KEY_REDIS_REGION)) {
            return redisService.getCacheList(ISysRegionService.KEY_REDIS_REGION);
        }
        return sysRegionMapper.selectAll();
    }

    @Override
    public List<RegionTreeNode> selectRegionTreeList() {
        List<SysRegion> allRegions = this.getAllRegions();
        // 构建树形结构
        RegionTreeBuilder treeBuilder = new RegionTreeBuilder();
        return treeBuilder.buildTree(allRegions);

    }

    @Override
    public List<SysRegion> getRegionsByParentId(String parentId) {
        if (redisService.hasKey(ISysRegionService.KEY_REDIS_REGION)) {
            List<SysRegion> allRegions = redisService.getCacheList(ISysRegionService.KEY_REDIS_REGION);
            if (parentId == null) {
                return allRegions.stream().filter(item -> item.getParentId() == null).collect(Collectors.toList());
            } else {
                return allRegions.stream().filter(item -> item.getParentId() != null && item.getParentId().equals(parentId)).collect(Collectors.toList());
            }
        } else {
            return this.sysRegionMapper.selectRegionsByParentId(parentId);
        }
    }


}