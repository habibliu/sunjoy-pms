package com.sunjoy.system.api;

import com.sunjoy.common.core.constant.SecurityConstants;
import com.sunjoy.common.core.constant.ServiceNameConstants;
import com.sunjoy.common.core.domain.R;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.system.api.domain.SysDept;
import com.sunjoy.system.api.domain.SysUser;
import com.sunjoy.system.api.factory.RemoteUserFallbackFactory;
import com.sunjoy.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 *
 * @author sunjoy
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 记录用户登录IP地址和登录时间
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    @PutMapping("/user/recordlogin")
    public R<Boolean> recordUserLogin(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取组织架构树
     *
     * @param dept
     * @return
     */
    @GetMapping("/user/deptTree")
    public AjaxResult deptTree(@RequestParam SysDept dept, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}