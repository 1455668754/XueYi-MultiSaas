package com.xueyi.auth.service.impl;

import com.xueyi.auth.form.RegisterBody;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.tenant.api.tenant.feign.RemoteTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录校验方法
 *
 * @author xueyi
 */
@Component
public class SysLoginService {

    @Autowired
    private SysLogService logService;

    @Autowired
    private RemoteTenantService remoteTenantService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     */
    public LoginUser login(String enterpriseName, String userName, String password) {
        // 企业账号||员工账号||密码为空 错误
        if (StrUtil.hasBlank(enterpriseName, userName, password)) {
            logService.recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "企业账号/员工账号/密码必须填写");
            AjaxResult.warn("企业账号/员工账号/密码必须填写");
        }
        // 企业账号不在指定范围内 错误
        if (enterpriseName.length() < OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH
                || enterpriseName.length() > OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH) {
            logService.recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "企业账号不在指定范围");
            AjaxResult.warn("企业账号不在指定范围");
        }

        // 员工账号不在指定范围内 错误
        if (userName.length() < OrganizeConstants.USERNAME_MIN_LENGTH
                || userName.length() > OrganizeConstants.USERNAME_MAX_LENGTH) {
            logService.recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "员工账号不在指定范围");
            AjaxResult.warn("员工账号不在指定范围");
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < OrganizeConstants.PASSWORD_MIN_LENGTH
                || password.length() > OrganizeConstants.PASSWORD_MAX_LENGTH) {
            logService.recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            AjaxResult.warn("用户密码不在指定范围");
        }
        UsernamePasswordAuthenticationToken loginBody = new UsernamePasswordAuthenticationToken(userName, password);
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), enterpriseName);
        loginMap.put(SecurityConstants.BaseSecurity.USER_NAME.getCode(), userName);
        loginMap.put(SecurityConstants.BaseSecurity.PASSWORD.getCode(), password);
        loginBody.setDetails(loginMap);
        Authentication authentication = authenticationManager.authenticate(loginBody);
        return (LoginUser) authentication.getPrincipal();
    }

    /**
     * 退出
     */
    public void logout(String sourceName, Long enterpriseId, String enterpriseName, Long userId, String userName, String userNick) {
        logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGOUT, "退出成功");
    }

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        // 注册租户信息
        R<?> registerResult = remoteTenantService.registerTenantInfo(registerBody.buildJson(), SecurityConstants.INNER);
        if (R.FAIL == registerResult.getCode()) {
            AjaxResult.warn(registerResult.getMsg());
        }
        // 注册逻辑补充完整后再增加日志
//        logService.recordLoginInfo(TenantConstants.Source.SLAVE.getCode(), SecurityConstants.EMPTY_TENANT_ID, registerBody.getTenant().getName(), SecurityConstants.EMPTY_USER_ID, registerBody.getUser().getUserName(), Constants.REGISTER, "注册成功");
    }

}