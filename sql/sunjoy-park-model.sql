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
	tenant_id   bigint(20)	default null 			   comment '租户id',
	ancestors   varchar(100)     default ''                 comment '祖级列表',
    park_name   varchar(60) default '' comment '车场名称',
    order_num   int(4)      default 0 comment '显示顺序',
	opu_id		bigint(20)  default 0 comment '经营单位ID',
	opu_name    varchar(60) default '' comment '经营单位名称',
    leader      varchar(20) default null comment '负责人',
    phone       varchar(11) default null comment '联系电话',
    park_type   char(1)     default null comment '车场类型 0--室外，1--室内,2--立体',
	region      varchar(10) default '' comment '区域',
	address     varchar(60) default '' comment '详细地址',
    total_lots  int(10)     default 0 comment '总车位数',
    status      char(1)     default '0' comment '状态（0未生效 1已生效,2注销）',
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
	tenant_id   bigint(20)	default null 			   comment '租户id',
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
	device_id     	bigint(20) 		not null	auto_increment 	comment '设备id',
    tenant_id   	bigint(20)		not null 			   		comment '租户id',
	opu_id			bigint(20)  	not null 					comment '经营单位Id',
	device_name   	varchar(60) 	not null  					comment '设备名称',
	device_model  	varchar(60) 	 							comment '设备型号',
	device_code  	varchar(60)  								comment '设备编号',
    functions   	varchar(20)  								comment '功能：REL--放行，DIS--显示，VOI--语音，SEN--感应',
    vendor      	varchar(60)      							comment '供应商',
	producer    	varchar(60)      							comment '生产商',
	params_parse	varchar(100)	 							comment '参数解释器,用来解设备参数的页面',
    status      	char(1)     	not null	default '0' 	comment '状态（0正常 1停用）',
    del_flag    	char(1)     	not null	default '0' 	comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64)  								comment '创建者',
    create_time 	datetime 									comment '创建时间',
    update_by   	varchar(64)  								comment '更新者',
    update_time 	datetime 									comment '更新时间',
	remark          varchar(500)                  				comment '备注',
    primary key (device_id)
) engine = innodb
  auto_increment = 1 comment = '车场设备表';
  
ALTER TABLE `pms_device` 
ADD UNIQUE INDEX `IDX_DEVICE_CODE`(`tenant_id`, `opu_id`, `device_code`) USING BTREE;
  
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
	id				bigint(20) 	not null 	auto_increment 	comment '车场id',
	device_id     	bigint(20) 	not null 					comment '设备id',
	lane_id     	bigint(20) 	not null 					comment '通道id',
	park_id     	bigint(20) 	not null 					comment '车场id',
	direction   	char(1) 		 						comment '通行方向:0－出，1－入，2－双向',
    status      	char(1)     							default '0' comment '状态（0正常 1停用）',
    del_flag    	char(1)     			default '0' 	comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64) 			 				comment '创建者',
    create_time 	datetime 								comment '创建时间',
    update_by   	varchar(64) 			 				comment '更新者',
    update_time 	datetime 								comment '更新时间',
	remark            varchar(500)    		default null    comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '通道设备关系表';
  
  
-- ----------------------------
-- 7、车辆注册表
-- ----------------------------
create table pms_vehicle
(
	vehicle_id     	bigint(20) 		not null 		auto_increment 	comment '车辆id',
	tenant_id   	bigint(20)		not null 			   			comment '租户id',
	opu_id   		bigint(20)		not null 			   			comment '经营单位id',
	license_plate   varchar(10) 	not null 						comment '车牌号码',
	brand     		varchar(20) 	default null 					comment '车辆品牌',
	model   		varchar(20) 	default null 					comment '车辆型号',
	vehicle_type 	varchar(20) 	default null 					comment '车辆类型',
    owner_name		varchar(20)  	not null 						comment '车主姓名',
	owner_phone     varchar(11) 	not null 						comment '车主电话',
	owner_addr		varchar(60) 	default null 					comment '车主地址',
	regist_date		datetime  		not null 						comment '登记日期',
	status      	char(1)     	default '0' 					comment '状态（0正常 1停用）',
    del_flag    	char(1)     	default '0' 					comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64) 	default null 					comment '创建者',
    create_time 	datetime 										comment '创建时间',
    update_by   	varchar(64) 	default null 					comment '更新者',
    update_time 	datetime 										comment '更新时间',
	remark          varchar(500)    default null               		comment '备注',
    primary key (vehicle_id)
) engine = innodb
  auto_increment = 1 comment = '车辆注册表';
ALTER TABLE `pms_vehicle` 
ADD INDEX `IDX_LICENSE`(`tenant_id`,`opu_id`,`license_plate`) USING BTREE;
-- ----------------------------
-- 8、车场通行规则
-- 规则：
-- １、车场满位等待
-- ２、临时出禁止入场
-- ３、白明单　
-- ４、黑名单　
-- ５、
-- ----------------------------
create table pms_park_rule
(
	rule_id     	bigint(20) 		not null auto_increment 	comment '规则id',
	rule_name   	varchar(20)  	NOT NULL 					comment '规则名称',
	tenant_id   	bigint(20)		NOT null 			   		comment '租户id',
	opu_id     		bigint(20)		not null 			   		comment '经营单位id',
	park_id   		bigint(20)		NOT null 			   		comment '车场id',
	level 			char(1)     	not null 	default '0' 	comment '规则适用级别:0-车场，1-道通,如果是通道级别，在params段中以json对象存储:{lanes:[{laneId:laneName},....]}',
	target 			char(1) 		not null	default '0' 	comment '规则适用目标对象:0-所有车，1-临时车，2-登记车,包括各种收费标准的车辆,在params段中以json对象存储:{services:[{serviceId:serviceName},....]}',
    day_range		char(1)			not null 	default '0'		comment '周期管制，0-全周，1-具体日,如果是具体日，在params段中以json对象存储:{days:[{1:周一},....]}',
	time_range		char(1)			not null	default '0'		comment '指定时间范围,0-否，1-是，如果是，在params段中以json对象存储:{times:[{startTime:08:00,entTime:12:00},....]}',
	allow_pass		char(1) 		not null 	default '0' 	comment '允许通行:1－是，0－否',
	direction  		char(1)			not null 	default '0' 	comment '通行方向:0--出场，１－入场，２－双方向',
	park_full		char(1)			not null	default '0'		comment '车场是否满位,0--否，1--是',
	target_expire	char(1)			not null 	default '0'		comment '服务是否过期：0-否，1--是',
	notify_message	varchar(50)  	 							comment '提示信息',
	notify_methods  varchar(20) 	 							comment '提示方式:0-屏显，１-语音，2-灯光',
	detail_params	text										comment '具体的规则参数,json格式',
	status      	char(1)     	default '0' 				comment '状态（0正常 1停用）',
    del_flag    	char(1)     	default '0' 				comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64) 	default '' 					comment '创建者',
    create_time 	datetime 									comment '创建时间',
    update_by   	varchar(64) 	default '' 					comment '更新者',
    update_time 	datetime 									comment '更新时间',
	remark          varchar(500)    default null               	comment '备注',
    primary key (rule_id)
) engine = innodb
  auto_increment = 1 comment = '车场通行规则';

-- ----------------------------
-- 8、车场收费价目表
-- ----------------------------
create table pms_park_price
(
	price_id     	bigint(20) 		not null 	auto_increment	comment '车辆id',
	tenant_id   	bigint(20)		not null 			   		comment '租户id',
	opu_id     		bigint(20)		not null 			   		comment '经营单位id',
	price_name     	varchar(60) 	not null 					comment '价格名称',
	free			char(1)			not null 	default '0' 	comment '是否免费:0-否,1-是',
    free_duration 	int(4) 			 							comment '免费时长',
	uniform_price	char(1)			 			 				comment '是否统一收费:0-否,1-是，如果不是统一收费，即要定义具体的收费明细,如果是，即定义一个统一的收费价格',
	price 			decimal(10,2) 								comment '单价',
    price_unit		varchar(10)  			 					comment '计费单位:TIMES-次数,MIN--分钟,HOUR--,DAY--,WEEK--,MONTH',
	price_quantity 	int(4)	 									comment '计费量',
	max_fee			decimal(10,2)  								comment '最高收费',
	max_unit 		varchar(10)   								comment '最高收费计费单位:TIMES-次数,DAY--,WEEK--,MONTH',
	max_quantity 	int(4)	 									comment '最高收费计费量',
	status      	char(1)  		not null  	default '0'		comment '状态（０--未生效 1--已生效,2--停用）',
    del_flag    	char(1)  		not null  	default '0' 	comment '删除标志（0代表存在 1代表删除），status为０时物理删除',
    create_by   	varchar(64)  								comment '创建者',
    create_time 	datetime 									comment '创建时间',
    update_by   	varchar(64)  								comment '更新者',
    update_time 	datetime 									comment '更新时间',
	remark          varchar(500)                   				comment '备注',
    primary key (price_id)
) engine = innodb
  auto_increment = 1 comment = '车场收费价目表'; 
  
ALTER TABLE `pms_park_price` 
ADD INDEX `IDX_OPU_ID`(`tenant_id`,`opu_id`) USING BTREE;

-- ----------------------------
-- 9、车场收费价目明细表
-- ----------------------------
create table pms_park_price_detail
(
	id     			bigint(20) 		not null 	auto_increment	comment '主键id',
	price_id     	bigint(20) 		not null 					comment '价格id',
	time_start  	varchar(20) 	not null 					comment '时段开始',
	time_end 	　　varchar(20)		not null					comment '时段结束',
	price 			decimal(10,2) 	not null 					comment '单价',
    price_unit		varchar(10)  	not null 					comment '计费单位:TIMES-次数,MIN--分钟,HOUR--,DAY--,WEEK--,MONTH',
	price_quantity 	int(4)			not null 					comment '计费量',
	status      	char(1)     				default '0' 	comment '状态（0正常 1停用）',
    del_flag    	char(1)     				default '0' 	comment '删除标志（0代表存在 1代表删除）',
    create_by   	varchar(64)  								comment '创建者',
    create_time 	datetime 									comment '创建时间',
    update_by   	varchar(64)  								comment '更新者',
    update_time 	datetime 									comment '更新时间',
	remark          varchar(500)                   				comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '车场收费价目明细表'; 

-- ----------------------------
-- 10、车场服务表
-- ----------------------------
create table pms_park_service
(
	service_id     		bigint(20) 	not null 	auto_increment 	comment '车辆id',
	tenant_id   		bigint(20)	not null 			   		comment '租户id',
	opu_id    			bigint(20)	not null 			   		comment '经营单位id',
	park_id     		bigint(20)	not null 			   		comment '车场id',
	price_id   			bigint(20)	not null 			   		comment '价格id',
	start_date 			datetime  								comment '启用日期',
	end_date 			datetime  								comment '停用日期',
	expired_allowed    	char(1) 	not null 	default '1' 	comment '过期是否允许出入场',
	expired_duration	int(4) 									comment '过期时间内仍保留身份',
	entry_message		varchar(30)  							comment '入场提示信息',
	exit_message		varchar(30)  							comment '出场提示信息',
	default_unregisted  char(1)					default 'N' 	comment '是否未登记车辆默认的收费标准，N--表示否，Y-表示是,一个车场只能设置一个',
	status      		char(1)     not null 	default '0' 	comment '状态（０--未生效 1--已生效,2--停用）',
    del_flag    		char(1)     not null 	default '0' 	comment '删除标志（0代表存在 1代表删除）',
    create_by   		varchar(64)  							comment '创建者',
    create_time 		datetime 								comment '创建时间',
    update_by   		varchar(64) 							comment '更新者',
    update_time 		datetime 								comment '更新时间',
	remark           	varchar(500)        					comment '备注',
    primary key (service_id)
) engine = innodb
  auto_increment = 1 comment = '车场服务表'; 
  
ALTER TABLE `pms_park_service` 
ADD INDEX `IDX_PARK_ID`(`tenant_id`, `park_id`) USING BTREE;
 
-- ----------------------------
-- 11、车辆服务表
-- ----------------------------
create table pms_vehicle_service
(
	id     			bigint(20) 	not null	auto_increment 	comment '主键id',
	service_id     	bigint(20)	not null 					comment '服务id',
	service_name    varchar(60)	not null 					comment '服务名称',
	tenant_id   	bigint(20)	not null 			   		comment '租户id',
	opu_id    		bigint(20)	not null 			   		comment '经营单位id',
	park_id     	bigint(20)	not null 			   		comment '车场id',
	vehicle_id     	bigint(20)	not null 			   		comment '车辆id',
	license_plate   varchar(10) not null 					comment '车牌号码',
	lot_nos			varchar(20)								comment '车位号码,多个车位用,分隔',
	start_date 		datetime  								comment '开始日期',
	end_date 		datetime  								comment '结束日期',
    owner_name		varchar(20)  not null 					comment '车主姓名,从车辆档案带过来',
	owner_phone     varchar(11)  							comment '车主电话,从车辆档案带过来',
	status      	char(1)     			default '0' 	comment '状态（０--未生效 1--已生效,2--停用）',
    del_flag    	char(1)     			default '0' 	comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64)  							comment '创建者',
    create_time 	datetime 								comment '创建时间',
    update_by   	varchar(64)  							comment '更新者',
    update_time 	datetime 								comment '更新时间',
	remark          varchar(500)                			comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '车辆服务表';
  
 ALTER TABLE `pms_vehicle_service` 
ADD INDEX `IDX_PARK_ID`(`park_id`) USING BTREE;
  
-- ----------------------------
-- 12、车辆出入场流水表
-- ----------------------------
create table pms_park_transaction
(
	trans_id     		bigint(20) 	not null	auto_increment 	comment '流水表id',
	tenant_id   		bigint(20)	not null 			   		comment '租户id',
	opu_id    			bigint(20)	not null 			   		comment '经营单位id',
	park_id     		bigint(20)	not null 			   		comment '车场id',
	park_name     		varchar(60)	not null 			   		comment '车场id',
	vehicle_id     		bigint(20)	 			   				comment '车辆id,只有登记车辆才有临停车没有',
	license_plate   	varchar(10) not null 					comment '车牌号码',
	entry_ref_id	    varchar(32)								comment '入场参照ID，来源于前端系统或者设备',
	entry_service_id    bigint(20)	 							comment '入场服务id',
	entry_lane_id		bigint(20)	 			   				comment '入场通道id',
	entry_lane_name		varchar(60)	 			   				comment '入场通道名称',
	entry_device_id		bigint(20)	 			   				comment '入场设备id',
	entry_time			datetime								comment '入场时间',
	entry_rel_time		datetime								comment '入场开闸放行时间',
	entry_rel_mode		char(1)									comment '入场开闸方式:0-自动放行，1-软件放行，2-遥控放行',
	lot_no				varchar(20)								comment '车位号码',
	exit_ref_id	    	varchar(32)								comment '入场参照ID，来源于前端系统或者设备',
	entry_service_id    bigint(20)	 							comment '出场服务id',
	exit_lane_id		bigint(20)	 			   				comment '出场通道id',
	exit_lane_name		varchar(60)	 			   				comment '出场通道名称',
	exit_device_id		bigint(20)	 			   				comment '出场设备id',
	exit_time			datetime								comment '出场时间',
	exit_rel_time		datetime								comment '出场开闸放行时间',
	exit_rel_mode		char(1)									comment '出场开闸方式:0-自动放行，1-软件放行，2-遥控放行',
	parking_duration	bigint(10)								comment '停车时长',
	status      		char(1)     			default '0' 	comment '状态（０--未出场 1--已出场）',
    del_flag    		char(1)     			default '0' 	comment '删除标志（0代表存在 2代表删除）',
    create_by   		varchar(64)  							comment '创建者',
    create_time 		datetime 								comment '创建时间',
    update_by   		varchar(64)  							comment '更新者',
    update_time 		datetime 								comment '更新时间',
	remark          	varchar(500)                			comment '备注',
    primary key (trans_id)
) engine = innodb
  auto_increment = 1 comment = '车辆出入场流水表';
  
-- ----------------------------
-- 13、车辆服务订单表
-- ----------------------------
create table pms_park_order
(
	order_id     	bigint(20) 	not null	auto_increment 	comment '订单id',
	order_type		char(2)		not null 					comment '订单类型:0-临停车收费，1--月租车收费，2--其他',
	trans_id     	bigint(20) 		 						comment '流水表id，月租费用订单没有流水',
	tenant_id   	bigint(20)	not null 			   		comment '租户id',
	opu_id    		bigint(20)	not null 			   		comment '经营单位id',
	park_id     	bigint(20)	not null 			   		comment '车场id',
	park_name     	varchar(60)	not null 			   		comment '车场名称',
	vehicle_id     	bigint(20)	not null 			   		comment '车辆id',
	license_plate   varchar(10) not null 					comment '车牌号码',
	service_id      bigint(20)	 							comment '服务id',
	service_name    varchar(60)	 							comment '收费标准名称',
	start_time 		datetime  								comment '开始时间',
	end_time 		datetime  								comment '结束时间',
	billing_amount	decimal(10,2) 							comment '计费金额',
	real_amount		decimal(10,2) 							comment '实收金额',
	free_amount		decimal(10,2) 							comment '扣减金额',
	order_detail	text									comment '订单明细，就是计费结果',
	status      	char(1)     			default '0' 	comment '状态（０--未支付  1--支付中  ,2--已支付 ，9--失效）',
    del_flag    	char(1)     			default '0' 	comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64)  							comment '创建者',
    create_time 	datetime 								comment '创建时间',
    update_by   	varchar(64)  							comment '更新者',
    update_time 	datetime 								comment '更新时间',
	remark          text                					comment '备注,用于存储计费结果明细',
    primary key (order_id)
) engine = innodb
  auto_increment = 1 comment = '车辆服务订单表';
  

-- ----------------------------
-- 14、车辆服务订单明细表
-- ----------------------------
create table pms_order_detail
(
	id				bigint(20) 	not null	auto_increment 	comment '主键id',
	order_id     	bigint(20) 	not null	 				comment '订单id',
	park_id     	bigint(20)	not null 			   		comment '车场id',
	vehicle_id     	bigint(20)	not null 			   		comment '车辆id',
	license_plate   varchar(10) not null 					comment '车牌号码',
	service_id      bigint(20)	 							comment '服务id',
	start_time 		datetime  								comment '开始时间',
	end_time 		datetime  								comment '结束时间',
	charge_price	decimal(10,2)							comment '计费单价',
	charge_quantity	int(10)									comment '计费量', 
	charge_unit		varchar(10)								comment '计费单位',
	amount			decimal(10,2) 							comment '金额',
	status      	char(1)     			default '0' 	comment '状态（０--未支付  1--已支付 ,2--失效）',
    del_flag    	char(1)     			default '0' 	comment '删除标志（0代表存在 2代表删除）',
    create_by   	varchar(64)  							comment '创建者',
    create_time 	datetime 								comment '创建时间',
    update_by   	varchar(64)  							comment '更新者',
    update_time 	datetime 								comment '更新时间',
	remark          varchar(500)                			comment '备注',
    primary key (order_id)
) engine = innodb
  auto_increment = 1 comment = '车辆服务订单明细表';
  
  
-- ----------------------------
-- 15 、车辆服务订单支付表
-- ----------------------------
create table pms_park_payment
(
	payment_id		bigint(20) 	not null	auto_increment 	comment '支付订单id',
	order_id     	bigint(20) 	not null	 				comment '订单id',
	trans_id     	bigint(20) 	not null	 				comment '流水表id',
	tenant_id   	bigint(20)	not null 			   		comment '租户id',
	opu_id    		bigint(20)	not null 			   		comment '经营单位id',
	park_id     	bigint(20)	not null 			   		comment '车场id',
	license_plate   varchar(10) not null 					comment '车牌号码',
	service_id      bigint(20)	 							comment '服务id',
	order_amount	decimal(10,2) 							comment '订单金额',
	payment_amount 	decimal(10,2) 							comment '支付金额',
	discount_amount	decimal(10,2) 							comment '优惠金额',
	payment_methods varchar(10) not null 					comment '支付方式:CASH-现金, WECHAT, ALIPAY',
	payment_channel varchar(10) not null 					comment '支付渠道:WECHAT, ALIPAY',
	payment_time	datetime								comment '支付时间',
	transaction_id 	varchar(60)								comment '第三方支付平台交易id',
	status      	char(1)     			default '0' 	comment '状态（０--PENDING 1--COMPLETED,2--REFUNDED,9--FAILED,',
    del_flag    	char(1)     			default '0' 	comment '删除标志（0代表存在 1代表删除）',
    create_by   	varchar(64)  							comment '创建者',
    create_time 	datetime 								comment '创建时间',
    update_by   	varchar(64)  							comment '更新者',
    update_time 	datetime 								comment '更新时间',
	remark          varchar(500)                			comment '备注',
    primary key (payment_id)
) engine = innodb
  auto_increment = 1 comment = '车辆服务订单支付表';
  

-- ----------------------------
-- 16 、折扣方案表
-- ----------------------------

-- ----------------------------
-- 17 、折扣表
-- ----------------------------
create table pms_park_discount
(  
	discount_id 	bigint(20) 		not null	auto_increment 	comment '折扣id',
	payment_id		bigint(20) 	 								comment '支付订单id',
	tenant_id   	bigint(20)		not null 			   		comment '租户id',
	opu_id    		bigint(20)		not null 			   		comment '经营单位id',
    discount_code 	VARCHAR(50) 	not null					comment '折扣码（可选）'
    discount_type 	varchar(10) 	NOT NULL 					comment '折扣类型（百分比或固定金额）PERCENTAGE, AMOUNT',
    discount_value 	DECIMAL(10, 2) 	NOT NULL         			comment '折扣值（例如 10% 或 $10）'
    min_fee 		DECIMAL(10, 2) 				DEFAULT 0   	comment '最低消费金额（可选）',
    start_date 		DATETIME 		NOT NULL,                   comment '开始时间',
    end_date 		DATETIME 		NOT NULL,                   comment '结束时间'
    usage_limit 	INT 						DEFAULT 1	    comment '使用限制（每个用户可使用次数）',
	use_time		datetime									comment '使用时间',
	status      	char(1)     				default '0' 	comment '状态（０--未使用 1--已使用）',
    del_flag    	char(1)     				default '0' 	comment '删除标志（0代表存在 1代表删除）',
    create_by   	varchar(64)  								comment '创建者',
    create_time 	datetime 									comment '创建时间',
    update_by   	varchar(64)  								comment '更新者',
    update_time 	datetime 									comment '更新时间',
	remark          varchar(500)                				comment '备注',
    primary key (discount_id)
) engine = innodb
  auto_increment = 1 comment = '折扣表';
  
