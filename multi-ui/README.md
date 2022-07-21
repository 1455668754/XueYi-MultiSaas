<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">XueYi-MultiSaas</h1>
<h4 align="center">基于 Vue3/TypeScript/Ant-Design UI 和 Spring Cloud & Alibaba/Mybatis-Plus 的多租户SaaS开发框架。</h4>
<p align="center">
    <a style="margin-right: 5px">
       <img src="https://img.shields.io/badge/XueYi--MultiSaas-v2.1.3-brightgreen" alt="xueYi-MultiSaas">
    </a>
    <a style="margin-right: 5px">
       <img src="https://gitee.com/xueyitiantang/XueYi-MultiSaas/badge/star.svg?theme=dark" alt="xueYi-MultiSaas">
    </a>
    <a style="margin-right: 5px">
       <img src="https://gitee.com/xueyitiantang/XueYi-MultiSaas/badge/fork.svg?theme=dark" alt="xueYi-MultiSaas">
    </a>
</p>

## 简介

基于 SpringBoot | Spring Cloud & Alibaba | Mybatis-Plus | Vue3 | vite2 | TypeScript | Ant-Design-Vue UI 的微服务多租户 SaaS 开发框架，为企业级多租户 Saas 及集团化应用提供快速开发解决方案。

## 特性

- **多重隔离控制**：物理隔离&&逻辑隔离，共享多租户&&隔离多租户
- **动态多源策略**：动态源增减&&租户动态识别源
- **租户控制优化**：通用数据&&混合租户&&独立租户多种租户模式，自动拦截，开发无感知
- **权限控制优化**：部门&&岗位&&用户多级可控，自动拦截，开发无感知
- **租户可配菜单**：多种混合模式控制租户菜单
- **组织管理优化**：更完善的组织架构操作与管理逻辑

## 交流

- 请移步右上角 **一键三连** :kissing_heart:
- QQ 群：[![加入QQ群](https://img.shields.io/badge/779343138-blue.svg)](https://jq.qq.com/?_wv=1027&k=zw11JJhj)
- 若发现 bug，请提 Issues。

## 预览

- **普通账户**

  > 企业账号：xueYi  
  > 员工账号：admin  
  > 密码：admin123

- **租管账户**

  > 企业账号：administrator  
  > 员工账号：admin  
  > 密码：admin123

- **演示**
  > [multi.xueyitt.cn](https://multi.xueyitt.cn)
- **文档**
  > [doc.xueyitt.cn](https://doc.xueyitt.cn)
- **视频**
  > [space.bilibili.com](https://space.bilibili.com/479745149)

## 准备

- [node](http://nodejs.org/) 和 [git](https://git-scm.com/) -项目开发环境
- [Vite](https://vitejs.dev/) - 熟悉 vite 特性
- [Vue3](https://v3.vuejs.org/) - 熟悉 Vue 基础语法
- [TypeScript](https://www.typescriptlang.org/) - 熟悉`TypeScript`基本语法
- [Es6+](http://es6.ruanyifeng.com/) - 熟悉 es6 基本语法
- [Vue-Router-Next](https://next.router.vuejs.org/) - 熟悉 vue-router 基本使用
- [Ant-Design-Vue](https://2x.antdv.com/docs/vue/introduce-cn/) - ui 基本使用
- [Mock.js](https://github.com/nuysoft/Mock) - mockjs 基本语法

## 安装使用

- 获取项目代码

```bash
git clone https://gitee.com/xueyitiantang/MultiSaas.git
```

- 安装依赖

```bash
cd MultiSaas-UI

pnpm install

```

- 运行

```bash
pnpm run -C admin dev
```

- 打包

```bash
pnpm build
```

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

- 感谢 anncwb 开源的[vue-vben-admin](https://github.com/vbenjs/vue-vben-admin)
- 感谢 antVue 团队开源的[ant-design-vue](https://github.com/vueComponent/ant-design-vue/)
