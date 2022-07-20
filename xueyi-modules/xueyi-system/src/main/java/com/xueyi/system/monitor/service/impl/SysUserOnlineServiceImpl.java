package com.xueyi.system.monitor.service.impl;

import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.monitor.domain.SysUserOnline;
import com.xueyi.system.monitor.service.ISysUserOnlineService;
import org.springframework.stereotype.Service;

/**
 * 在线用户 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr    登录地址
     * @param loginUser 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser loginUser) {
        if (StringUtils.equals(ipaddr, loginUser.getIpaddr())) {
            return loginUserToUserOnline(loginUser);
        }
        return null;
    }

    /**
     * 通过用户账号查询信息
     *
     * @param userName  用户账号
     * @param loginUser 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser loginUser) {
        if (StringUtils.equals(userName, loginUser.getUserName())) {
            return loginUserToUserOnline(loginUser);
        }
        return null;
    }

    /**
     * 通过登录地址/用户账号查询信息
     *
     * @param ipaddr    登录地址
     * @param userName  用户账号
     * @param loginUser 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser loginUser) {
        if (StringUtils.equals(ipaddr, loginUser.getIpaddr()) && StringUtils.equals(userName, loginUser.getUserName())) {
            return loginUserToUserOnline(loginUser);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param loginUser 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser loginUser) {
        if (StringUtils.isNull(loginUser)) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(loginUser.getToken());
        sysUserOnline.setUserName(loginUser.getUserName());
        sysUserOnline.setUserNick(loginUser.getUser().getNickName());
        sysUserOnline.setIpaddr(loginUser.getIpaddr());
        sysUserOnline.setLoginTime(loginUser.getLoginTime());
        return sysUserOnline;
    }
}
