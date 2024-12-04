package com.sunjoy.park.client.service;

import com.sunjoy.common.core.constant.CacheConstants;
import com.sunjoy.common.core.exception.ServiceException;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.park.client.entity.SysUser;
import org.springframework.stereotype.Component;

/**
 * 登录密码方法
 *
 * @author sunjoy
 */
@Component
public class SysPasswordService {

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password) {
        String username = user.getUserName();


        if (!matches(user, password)) {


            throw new ServiceException("用户不存在/密码错误");
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }


}