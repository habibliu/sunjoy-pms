package com.sunjoy.park.client.service;

import com.sunjoy.common.core.constant.UserConstants;
import com.sunjoy.common.core.enums.UserStatus;
import com.sunjoy.common.core.exception.ServiceException;
import com.sunjoy.common.core.utils.StringUtils;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.park.client.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录校验方法
 *
 * @author sunjoy
 */
@Service
public class SysLoginService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPasswordService passwordService;


    /**
     * 登录
     */
    public SysUser login(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {

            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
            || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {

            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
            || username.length() > UserConstants.USERNAME_MAX_LENGTH) {

            throw new ServiceException("用户名不在指定范围");
        }

        // 查询用户信息
        SysUser sysUser = sysUserService.findSysUserByUsername(username);

        if (null == sysUser) {
            throw new ServiceException("用户不存在");
        }


        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {

            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        passwordService.validate(sysUser, password);


        return sysUser;
    }


    public void logout(String loginName) {
        //todo 登出

    }

    /**
     * 注册
     */
    public void register(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
            || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
            || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUserService.createUser(sysUser);

    }
}