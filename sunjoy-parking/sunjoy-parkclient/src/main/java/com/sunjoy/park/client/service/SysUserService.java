package com.sunjoy.park.client.service;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.park.client.entity.SysUser;
import com.sunjoy.park.client.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

/**
 * 系统用户服务类
 *
 * @author Habib
 * @date 2024/11/28
 */
@Service
public class SysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;

    public SysUser findSysUserByUsername(String username) {
        // 创建一个 SysUser 对象作为查询条件
        SysUser probe = new SysUser();
        // 设置需要匹配的用户名
        probe.setUserName(username);

        // 创建匹配器，指定查询方式
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact());

        // 创建 Example 对象
        Example<SysUser> example = Example.of(probe, matcher);

        // 查询并返回结果
        return this.sysUserRepository.findOne(example).orElse(null);
    }

    public void createUser(SysUser sysUser) {
        this.sysUserRepository.save(sysUser);
    }

    public void initUser() {
        SysUser sysUser = findSysUserByUsername("admin");
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setUserName("admin");
            sysUser.setPassword(SecurityUtils.encryptPassword("123456"));
            sysUser.setStatus("0");
            this.sysUserRepository.save(sysUser);
        }
    }
}