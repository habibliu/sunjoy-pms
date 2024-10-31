DROP DATABASE IF EXISTS `sunjoy-parkmodel`;

CREATE DATABASE  `sunjoy-parkmodel` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `sunjoy-parkmodel`;

-- ----------------------------
-- 1、车场表
-- ----------------------------

create table pms_park
(
    park_id     bigint(20) not null auto_increment comment '车场id',
    parent_id   bigint(20)  default 0 comment '父区域id',
	ancestors   varchar(100)     default ''                 comment '祖级列表',
    park_name   varchar(60) default '' comment '部门名称',
    order_num   int(4)      default 0 comment '显示顺序',
	opu_id		bigint(20)  default 0 comment '经营单位ID',
	opu_name    varchar(60) default '' comment '经营单位名称',
    leader      varchar(20) default null comment '负责人',
    phone       varchar(11) default null comment '联系电话',
    park_type   char(1)     default null comment '车场类型',
	region      varchar(10) default '' comment '区域',
	address     varchar(60) default '' comment '详细地址',
    total_lots  int(10)     default 0 comment '总车位数',
    status      char(1)     default '0' comment '状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
	remark            varchar(500)    default null               comment '备注',
    primary key (park_id)
) engine = innodb
  auto_increment = 1 comment = '车场档案表';


-- ----------------------------
-- 2、通道表
-- ----------------------------

create table pms_lane
(
	lane_id     bigint(20) not null auto_increment comment '车场id',
	lane_name   varchar(60) default '' comment '通道名称',
	opu_id		bigint(20)  default 0 comment '经营单位Id',
    rap         char(1)     default '0' comment '收费后才放行- release after payment',
	link_outer	char(1)		default '1' comment '连通外围',
    status      char(1)     default '0' comment '状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
	remark            varchar(500)    default null               comment '备注',
    primary key (lane_id)
) engine = innodb
  auto_increment = 1 comment = '通道表';
  
  
 -- ----------------------------
-- 3、车场通道表关系表
-- ----------------------------
create table pms_park_lane
(
	id			 bigint(20) not null auto_increment comment '自增主键',
	lane_id     bigint(20) not null  comment '通道id',
    park_id   bigint(20)  not null comment '车场id',
    direction   char(1) default '' comment '通行方向:0－出，1－入，2－双向',
    status      char(1)     default '0' comment '状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
	remark            varchar(500)    default null               comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '车场通道表关系表';
  
  
-- ----------------------------
-- 4、车场设备表
-- ----------------------------

create table pms_device
(
	device_id     bigint(20) not null auto_increment comment '设备id',
    device_name   varchar(60) default '' comment '设备名称',
	device_model  varchar(60) default '' comment '设备型号',
	opu_id		bigint(20)  default 0 comment '经营单位Id',
    functions   varchar(20) default '' comment '功能：REL--放行，DIS--显示，VOI--语音，SEN--感应',
    vendor      varchar(60)     default '0' comment '供应商',
	producer    varchar(60)     default '0' comment '生产商',
	params_parse	varchar(100)	default '' comment '参数解释器,用来解设备参数的页面',
    status      char(1)     default '0' comment '状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
	remark            varchar(500)    default null               comment '备注',
    primary key (device_id)
) engine = innodb
  auto_increment = 1 comment = '车场设备表';
  
  
-- ----------------------------
-- 5、车场设备参数表
-- ----------------------------
create table pms_device_params
(
	id			 bigint(20) not null auto_increment comment '车场id',
	device_id     bigint(20) not null comment '设备id',
	device_params  longtext default null comment '设备参数',
    status      char(1)     default '0' comment '状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
	remark            varchar(500)    default null               comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '车场设备参数表';
  

-- ----------------------------
-- 6、通道设备关系表
-- ----------------------------
create table pms_lane_device
(
	id			 bigint(20) not null auto_increment comment '车场id',
	device_id     bigint(20) not null comment '设备id',
	lane_id     bigint(20) not null comment '通道id',
	park_id     bigint(20) not null comment '车场id',
	direction   char(1) default '' comment '通行方向:0－出，1－入，2－双向',
    status      char(1)     default '0' comment '状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
	remark            varchar(500)    default null               comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '通道设备关系表';