package com.xueyi.auth.service;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.MapUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份验证 服务层
 *
 * @author xueyi
 */
public interface IUserDetailsService extends Ordered {

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return NumberUtil.Zero;
    }

    /**
     * 登录验证
     *
     * @param principal 登录信息
     * @return 用户信息
     */
    default UserDetails loadUser(Object principal) {
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
    UserDetails loadUser(String enterpriseName, String userName, String password) throws UsernameNotFoundException;

}

