package com.sunjoy.common.security.utils;


import com.sunjoy.common.core.constant.CacheConstants;
import com.sunjoy.common.core.constant.SecurityConstants;
import com.sunjoy.common.core.constant.TokenConstants;
import com.sunjoy.common.core.context.SecurityContextHolder;
import com.sunjoy.common.core.utils.ServletUtils;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.core.utils.StringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.system.api.domain.SysDept;
import com.sunjoy.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 权限获取工具类
 *
 * @author sunjoy
 */
public class SecurityUtils {
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername() {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    public static Long getTenantId() {
        LoginUser loginUser = SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
        return loginUser == null ? 1L : loginUser.getTenantId();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 获取用户授权的部门ID，即经营实体的ID
     *
     * @param userId
     * @param deptId
     * @return
     */
    public static List<Long> getAuthDeptIds(Long userId, Long deptId) {
        try {
            RedisService redisService = SpringUtils.getBean(com.sunjoy.common.redis.service.RedisService.class);
            Long currentTenantId = getTenantId();
            List<SysDept> tenantDepts = redisService.getCacheList(CacheConstants.SYS_DEPT_LIST_KEY + currentTenantId);
            SysDept dept = tenantDepts.stream().filter(item -> Objects.equals(deptId, item.getDeptId())).findFirst().orElse(null);
            if (null != dept) {
                //TODO 应该还要取出userId的授权部署与下面的匹配，即相互存在才返回
                System.out.println("当前用户是" + userId);
                // 过滤出 ancestors 以当前部门的ancestors开头的记录，并提取 dept_id
                return tenantDepts.stream()
                        .filter(item -> item.getAncestors().startsWith(dept.getAncestors()))
                        .map(SysDept::getDeptId)
                        .collect(Collectors.toList());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}