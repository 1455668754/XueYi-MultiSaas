package com.xueyi.auth.support.password;

import com.xueyi.auth.service.ISysLogService;
import com.xueyi.auth.support.base.AuthenticationBaseConverter;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.SecurityConstants.LoginParam;
import com.xueyi.common.core.constant.basic.SecurityConstants.OauthType;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

/**
 * 自定义验证转换器 | 密码模式
 *
 * @author xueyi
 */
public class AuthenticationPasswordConverter extends AuthenticationBaseConverter<AuthenticationPasswordToken> {

    /**
     * 判断授权类型
     *
     * @param grantType 授权类型
     * @return 结果
     */
    @Override
    public boolean support(String grantType) {
        return StrUtil.equals(OauthType.PASSWORD.getCode(), grantType);
    }

    /**
     * 构建token
     *
     * @param clientPrincipal      身份验证对象
     * @param requestedScopes      请求范围
     * @param additionalParameters 扩展信息
     * @return Token
     */
    @Override
    public AuthenticationPasswordToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new AuthenticationPasswordToken(clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 参数校验
     *
     * @param request 请求体
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = ServletUtil.getParameters(request);

        // 企业账号校验
        String enterpriseName = parameters.getFirst(LoginParam.ENTERPRISE_NAME.getCode());
        String userName = parameters.getFirst(LoginParam.USER_NAME.getCode());
        String password = parameters.getFirst(LoginParam.PASSWORD.getCode());

        // 企业账号||员工账号||密码为空 错误
        if (StrUtil.isBlank(enterpriseName) || StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            SpringUtil.getBean(ISysLogService.class).recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "企业账号/员工账号/密码必须填写");
            throw new UsernameNotFoundException("企业账号/员工账号/密码必须填写");
        }

        // 企业账号不在指定范围内 错误
        if (enterpriseName.length() < OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH
                || enterpriseName.length() > OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH) {
            SpringUtil.getBean(ISysLogService.class).recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "企业账号不在指定范围");
            throw new UsernameNotFoundException("企业账号不在指定范围");
        }

        // 员工账号不在指定范围内 错误
        if (userName.length() < OrganizeConstants.USERNAME_MIN_LENGTH
                || userName.length() > OrganizeConstants.USERNAME_MAX_LENGTH) {
            SpringUtil.getBean(ISysLogService.class).recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "员工账号不在指定范围");
            throw new UsernameNotFoundException("员工账号不在指定范围");
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < OrganizeConstants.PASSWORD_MIN_LENGTH
                || password.length() > OrganizeConstants.PASSWORD_MAX_LENGTH) {
            SpringUtil.getBean(ISysLogService.class).recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new UsernameNotFoundException("用户密码不在指定范围");
        }
    }
}
