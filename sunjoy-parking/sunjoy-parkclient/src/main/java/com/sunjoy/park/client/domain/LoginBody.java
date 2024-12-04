package com.sunjoy.park.client.domain;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author sunjoy
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;


}