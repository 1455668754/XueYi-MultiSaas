package com.xueyi.system.monitor.domain;

import lombok.Data;

/**
 * 系统服务 | 监控模块 | 在线用户监控
 *
 * @author xueyi
 */
@Data
public class SysUserOnline {

    /** 会话编号 */
    private String tokenId;

    /** 用户账号 */
    private String userName;

    /** 用户名称 */
    private String userNick;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地址 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录时间 */
    private Long loginTime;

}
