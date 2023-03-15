package com.xueyi.auth.login.base.impl;

import com.xueyi.auth.login.base.IUserDetailsService;
import com.xueyi.auth.service.ISysLogService;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.MapUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.RemoteLoginService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录验证 | 密码模式
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
     * 是否支持此客户端校验
     *
     * @param clientId    目标客户端
     * @param grantType   授权类型
     * @param accountType 账户类型
     * @return 结果
     */
    @Override
    public boolean support(String clientId, String grantType, String accountType) {
        return StrUtil.equals(SecurityConstants.GrantType.PASSWORD.getCode(), grantType) && StrUtil.equals(SecurityConstants.AccountType.ADMIN.getCode(), grantType);
    }

    /**
     * 登录验证
     *
     * @param principal 登录信息
     * @return 用户信息
     */
    @Override
    @SneakyThrows
    public UserDetails loadUser(Object principal) {
        Map<String, String> loginMap = ConvertUtil.toMap(String.class, String.class, principal);
        if (MapUtil.isEmpty(loginMap)) {
            loginMap = new HashMap<>();
        }
        String enterpriseName = loginMap.get(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode());
        String userName = loginMap.get(SecurityConstants.BaseSecurity.USER_NAME.getCode());
        String password = loginMap.get(SecurityConstants.BaseSecurity.PASSWORD.getCode());
        return loadUser(enterpriseName, userName, password);
    }

    /**
     * 登录验证
     *
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param password       密码
     * @return 用户信息
     */
    @SneakyThrows
    public LoginUser loadUser(String enterpriseName, String userName, String password) {
        // 查询登录信息
        R<LoginUser> loginInfoResult = remoteLoginService.getLoginInfoInner(enterpriseName, userName, password);
        if (loginInfoResult.isFail()) {
            throw new UsernameNotFoundException("服务调用失败，请稍后再试！");
        } else if (ObjectUtil.isNull(loginInfoResult.getData())) {
            logService.recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, loginInfoResult.getMsg());
            throw new UsernameNotFoundException("企业账号/员工账号/密码错误，请检查！");
        }
        LoginUser loginUser = loginInfoResult.getData();
        Long enterpriseId = loginUser.getEnterpriseId();
        String sourceName = loginUser.getSourceName();
        SysUserDto user = loginUser.getUser();
        Long userId = user.getId();
        String userNick = user.getNickName();
        if (BaseConstants.Status.DISABLE.getCode().equals(loginUser.getUser().getStatus())) {
            logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new UsernameNotFoundException("对不起，您的账号：" + userName + " 已停用！");
        }
        loginUser.setPassword(SecurityConstants.BCRYPT + loginUser.getPassword());
        return loginUser;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
