package com.xueyi.auth.service.impl;

import com.xueyi.auth.service.ISysLogService;
import com.xueyi.auth.service.IUserDetailsService;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.RemoteLoginService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 登录验证
 *
 * @author xueyi
 */
@Component
public class UserDetailsServiceImpl implements IUserDetailsService {

    @Autowired
    private ISysLogService logService;

    @Autowired
    private RemoteLoginService remoteLoginService;

    /**
     * 登录验证
     */
    @Override
    public LoginUser loadUser(String enterpriseName, String userName, String password) throws UsernameNotFoundException {
        // 查询登录信息
        R<LoginUser> loginInfoResult = remoteLoginService.getLoginInfoInner(enterpriseName, userName, password, SecurityConstants.INNER);
        if (loginInfoResult.isFail()) {
            AjaxResult.warn("当前访问人数过多，请稍后再试！");
        } else if (ObjectUtil.isNull(loginInfoResult.getData())) {
            logService.recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, loginInfoResult.getMsg());
            AjaxResult.warn("企业账号/员工账号/密码错误，请检查！");
        }
        LoginUser loginUser = loginInfoResult.getData();
        Long enterpriseId = loginUser.getEnterpriseId();
        String sourceName = loginUser.getSourceName();
        SysUserDto user = loginUser.getUser();
        Long userId = user.getId();
        String userNick = user.getNickName();
        if (BaseConstants.Status.DISABLE.getCode().equals(loginUser.getUser().getStatus())) {
            logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            AjaxResult.warn("对不起，您的账号：" + userName + " 已停用！");
        }

        logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGIN_SUCCESS, "登录成功");
        return loginUser;
    }
}
