package com.xueyi.auth.login.base;

import com.xueyi.common.core.utils.core.NumberUtil;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 身份验证 服务层
 *
 * @author xueyi
 */
public interface IUserDetailsService extends Ordered {

    /**
     * 是否支持此客户端校验
     *
     * @param clientId    目标客户端
     * @param grantType   授权类型
     * @param accountType 账户类型
     * @return 结果
     */
    boolean support(String clientId, String grantType, String accountType);

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
    UserDetails loadUser(Object principal);

}

