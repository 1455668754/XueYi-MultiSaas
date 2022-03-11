<h1 align="center">XueYi-MultiSaas</h1>

<p align="center">
    <a>
       <img src="https://img.shields.io/badge/XueYi--MultiSaas-v2.1.3-brightgreen" alt="xueYi-MultiSaas">
    </a>
    <a>
       <img src="https://img.shields.io/badge/Spring%20Cloud%20%26%20Alibaba-2021.1-brightgreen" alt="xueYi-MultiSaas">
    </a>
    <a>
       <img src="https://img.shields.io/badge/Vue3-Ant--Design--Vue-green" alt="xueYi-MultiSaas">
    </a>
    <a>
       <img src="https://img.shields.io/badge/vite2-TypeScript-green" alt="xueYi-MultiSaas">
    </a>
    <a>
       <img src="https://img.shields.io/badge/Mybatis--Plus-3.4.0%2B-brightgreen" alt="xueYi-MultiSaas">
    </a>
    <a>
       <img src="https://gitee.com/xueyitiantang/XueYi-MultiSaas/badge/star.svg?theme=dark" alt="xueYi-MultiSaas">
    </a>
    <a>
       <img src="https://gitee.com/xueyitiantang/XueYi-MultiSaas/badge/fork.svg?theme=dark" alt="xueYi-MultiSaas">
    </a>
</p>

## 简介
基于SpringBoot | Spring Cloud & Alibaba | Mybatis-Plus | Vue3 | vite2 | TypeScript | Ant-Design-Vue UI的多租户SaaS开发框架，为企业级多租户Saas及集团化应用提供快速开发解决方案。

## 特性
- **多租户Saas**： 物理隔离&&逻辑隔离 --- 共享多租户&&隔离多租户
- **动态多源策略**：租户-策略-数据源 --- 动态源增减与租户策略配置
- **素材管理模块**：素材集中管理 --- 文件&&图片统一管理，配置
- **权限控制优化**：角色-模块-菜单,自定义拦截器 --- 优化角色控制逻辑
- **租户菜单层级**：租管可动态指定租户可用模块或菜单
- **组织管理优化**：部门-岗位-用户 --- 更完善的组织架构操作与管理逻辑
- **微聚合多前端**：前端素材&&组件公用 --- 降低系统重复冗余

## UI
前端地址： [MultiSaas-UI](https://gitee.com/xueyitiantang/MultiSaas-UI)

## 交流
- 请移步右上角  **一键三连** :kissing_heart:
- QQ群：[![加入QQ群](https://img.shields.io/badge/779343138-blue.svg)](https://jq.qq.com/?_wv=1027&k=zw11JJhj)
- 若发现bug，请提Issues。

## 预览
- **普通账户**
  > 企业账号：xueYi   
  员工账号：admin   
  密码：admin123

- **租管账户**
  > 企业账号：administrator   
  员工账号：admin   
  密码：admin123

- **演示**
  >https://multi.xueyitt.cn
- **文档**
  >https://doc.xueyitt.cn
- **视频**
  >https://space.bilibili.com/479745149

~~~
com.xueyi     
├── xueyi-gateway         // 网关模块 [8080]
├── xueyi-auth            // 认证中心 [9200]
├── xueyi-api             // 接口模块
│       ├── xueyi-api-system                          // 系统接口
│       ├── xueyi-api-tenant                          // 租管接口
│       └── xueyi-api-job                             // 调度接口
├── xueyi-common          // 通用模块
│       ├── xueyi-common-core                         // 核心模块
│       ├── xueyi-common-datascope                    // 权限范围
│       ├── xueyi-common-datasource                   // 多数据源
│       ├── xueyi-common-log                          // 日志记录
│       ├── xueyi-common-redis                        // 缓存服务
│       ├── xueyi-common-security                     // 安全模块
│       ├── xueyi-common-swagger                      // 系统接口
│       └── xueyi-common-web                          // 基类封装
├── xueyi-modules         // 业务模块
│       ├── xueyi-file                                // 文件服务 [9300]
│       ├── xueyi-gen                                 // 代码生成 [9400]
│       ├── xueyi-job                                 // 定时任务 [9500]
│       ├── xueyi-system                              // 系统模块 [9600]
│               ├── authority                         // 权限模块
│               ├── dict                              // 参数字典
│               ├── material                          // 素材模块
│               ├── monitor                           // 监控模块
│               ├── notice                            // 公告模块
│               └── organize                          // 组织模块
│       └── xueyi-tenant                              // 租管模块 [9700]
│               ├── source                            // 多数据源
│               └── tenant                            // 租户策略
├── xueyi-visual          // 图形化管理模块
│       └── xueyi-visual-monitor                      // 监控中心 [9100]
└── pom.xml                // 公共依赖
~~~

## 架构

<img src="https://images.gitee.com/uploads/images/2021/1108/172436_9deff9ff_7382127.png"/>

## 演示
<table>
    <tr>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/1.png"/></td>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/3.png"/></td>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/4.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/5.png"/></td>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/6.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/7.png"/></td>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/8.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/9.png"/></td>
        <td><img src="https://gitee.com/xueyitiantang/images/raw/master/10.png"/></td>
    </tr>
</table>

## 开源
**源于开源，回归开源**
* 感谢若依开源的[RuoYi-Cloud](https://gitee.com/y_project/RuoYi-Cloud)
* 感谢苞米豆开源的[mybatis-plus](https://github.com/baomidou/mybatis-plus)
* 感谢小锅盖开源的[dynamic](https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter)