package com.xueyi.system.monitor.service;

import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.monitor.domain.SysUserOnline;

/**
 * 系统服务 | 监控模块 | 在线用户监控 服务层
 *
 * @author xueyi
 */
public interface ISysUserOnlineService {

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr    登录地址
     * @param loginUser 用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser loginUser);

    /**
     * 通过用户账号查询信息
     *
     * @param userName  用户账号
     * @param loginUser 用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByUserName(String userName, LoginUser loginUser);

    /**
     * 通过登录地址/用户账号查询信息
     *
     * @param ipaddr    登录地址
     * @param userName  用户账号
     * @param loginUser 用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser loginUser);

    /**
     * 设置在线用户信息
     *
     * @param loginUser 用户信息
     * @return 在线用户
     */
    SysUserOnline loginUserToUserOnline(LoginUser loginUser);
}
