package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.security.service.TokenService;
import com.sunjoy.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/12/3
 */
@RestController
@RequestMapping("/adapter")
public class PmsParkAdapterController {
    @Autowired
    private TokenService tokenService;

    @GetMapping("/heartbeat")
    public ResponseEntity<String> adapterHeartBeat(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        String token = authorization.substring(7); // 去掉 "Bearer " 前缀
        LoginUser loginUser = tokenService.getLoginUser(token);
        if (null == loginUser) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failure");
        }
        tokenService.refreshToken(loginUser);
        // 返回成功响应
        return ResponseEntity.ok("success");
    }
}