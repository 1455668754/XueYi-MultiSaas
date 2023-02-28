package com.xueyi.auth.service.impl;

import com.xueyi.auth.service.ISysLogService;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.ip.IpUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.feign.RemoteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志记录 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysLogServiceImpl implements ISysLogService {

    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 记录登录信息 | 无企业信息
     *
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    @Override
    public void recordLoginInfo(String enterpriseName, String userName, String status, String message) {
        recordLoginInfo(TenantConstants.Source.SLAVE.getCode(), SecurityConstants.EMPTY_TENANT_ID, enterpriseName, SecurityConstants.EMPTY_USER_ID, userName, StrUtil.EMPTY, status, message);
    }

    /**
     * 记录登录信息 | 无用户信息
     *
     * @param sourceName     索引数据源源
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    @Override
    public void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, String userName, String status, String message) {
        recordLoginInfo(sourceName, enterpriseId, enterpriseName, SecurityConstants.EMPTY_USER_ID, userName, StrUtil.EMPTY, status, message);
    }

    /**
     * 记录登录信息
     *
     * @param loginUser 用户登录信息
     * @param status    状态
     * @param message   消息内容
     */
    @Override
    public <LoginUser extends BaseLoginUser<?>> void recordLoginInfo(LoginUser loginUser, String status, String message) {
        recordLoginInfo(loginUser.getSourceName(), loginUser.getEnterpriseId(), loginUser.getEnterpriseName(), loginUser.getUserId(), loginUser.getUserName(), loginUser.getNickName(), status, message);
    }

    /**
     * 记录登录信息
     *
     * @param sourceName     索引数据源源
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userId         用户Id
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    @Override
    public void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, Long userId, String userName, String userNick, String status, String message) {
        SysLoginLogDto loginInfo = new SysLoginLogDto();
        loginInfo.setEnterpriseId(enterpriseId);
        loginInfo.setEnterpriseName(enterpriseName);
        loginInfo.setUserId(userId);
        loginInfo.setUserName(userName);
        loginInfo.setUserNick(userNick);
        loginInfo.setIpaddr(IpUtil.getIpAddr(ServletUtil.getRequest()));
        loginInfo.setMsg(message);
        // 日志状态
        if (StrUtil.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginInfo.setStatus(DictConstants.DicStatus.NORMAL.getCode());
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginInfo.setStatus(DictConstants.DicStatus.FAIL.getCode());
        }
        remoteLogService.saveLoginInfo(loginInfo, enterpriseId, sourceName, SecurityConstants.INNER);
    }
}
