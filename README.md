## 尚久停车管理平台简介

尚久停车管理平台是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 采用前后端分离的模式，微服务版本前端(基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue))。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。
*

提供了技术栈（[Vue3](https://v3.cn.vuejs.org) [Element Plus](https://element-plus.org/zh-CN) [Vite](https://cn.vitejs.dev)
）版本[RuoYi-Cloud-Vue3](https://github.com/yangzongzhuan/RuoYi-Cloud-Vue3)，保持同步更新。

* 建议使用Kubernetes部署所有服务与中间件，如担心

#### 友情链接 [若依/RuoYi-Cloud](https://gitee.com/zhangmrit/ruoyi-cloud) Ant Design版本。

## 系统模块

~~~
com.sunjoy
├── sunjoy-ui        // 前端UI
│       └── sunjoy-ui-admin                           // 后台管理前端 [80]
│       └── sunjoy-ui-position                        // 岗停收费员前端 [80]
│       └── sunjoy-ui-mobie                           // 移动岗停收费员前端 [80]
├── sunjoy-gateway         // 网关模块 [8080]
├── sunjoy-auth            // 认证中心 [8081]
├── sunjoy-api             // 接口模块
│       └── sunjoy-api-system                          // 系统接口
├── sunjoy-common          // 通用模块
│       └── sunjoy-common-core                         // 核心模块
│       └── sunjoy-common-datascope                    // 权限范围
│       └── sunjoy-common-datasource                   // 多数据源
│       └── sunjoy-common-log                          // 日志记录
│       └── sunjoy-common-redis                        // 缓存服务
│       └── sunjoy-common-seata                        // 分布式事务
|       └── sunjoy-common-rabbitmq                     // 异步通讯服务
│       └── sunjoy-common-security                     // 安全模块
│       └── sunjoy-common-swagger                      // 系统接口
├── sunjoy-psv         // 公共服务模块
│       └── sunjoy-system                              // 系统模块 [8201]
│       └── sunjoy-gen                                 // 代码生成 [8202]
│       └── sunjoy-job                                 // 定时任务 [8203]
│       └── sunjoy-file                                // 文件服务 [8204]                              //     
├── sunjoy-parking                                     // 车场业务模块
│       └── sunjoy-park-model                          // 车场模型 [8301]
│       └── sunjoy-park-station                        // 车场岗亭 [8302]
│       └── sunjoy-park-charging                       // 车场收费 [8303]
│       └── sunjoy-park-device                         // 车场设备 [8304]
├── sunjoy-visual          // 图形化管理模块
│       └── sunjoy-visual-monitor                      // 监控中心 [8400]
├── pom.xml                 // 公共依赖
├── sql                     //数据库脚本
├── docker                  //Docker构建脚本
├── nacos-conf              //Nacos配置文件