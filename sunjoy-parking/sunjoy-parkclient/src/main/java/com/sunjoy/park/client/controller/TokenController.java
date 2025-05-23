package com.sunjoy.park.client.controller;

import com.sunjoy.common.core.domain.R;
import com.sunjoy.common.core.utils.JwtUtils;
import com.sunjoy.common.core.utils.StringUtils;
import com.sunjoy.common.security.auth.AuthUtil;
import com.sunjoy.common.security.service.TokenService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.park.client.domain.LoginBody;
import com.sunjoy.park.client.entity.SysUser;
import com.sunjoy.park.client.service.SysLoginService;
import com.sunjoy.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * token 控制
 *
 * @author sunjoy
 */
@RestController
@RequestMapping("/auth")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody form) {
        // 用户登录
        SysUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(userInfo.getId());
        loginUser.setUsername(userInfo.getUserName());

        // 获取登录token
        return R.ok(tokenService.createToken(loginUser));
    }

    @DeleteMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("/refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

}