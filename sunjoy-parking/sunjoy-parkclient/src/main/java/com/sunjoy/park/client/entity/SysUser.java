package com.sunjoy.park.client.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 前端系统用户
 *
 * @author Habib
 * @date 2024/11/28
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userName;

    private String password;

    private String nickName;

    private String status;
}