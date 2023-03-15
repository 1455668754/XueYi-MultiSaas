package com.xueyi.auth.login.base;

import com.xueyi.common.core.utils.core.NumberUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

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
     * @param clientId    目标客户端
     * @param grantType   授权类型
     * @param accountType 账户类型
     * @return 结果
     */
    default boolean support(String clientId, String grantType, String accountType){
        return support(grantType, accountType);
    }

    /**
     * 校验授权类型与账户类型
     *
     * @param grantType   授权类型
     * @param accountType 账户类型
     * @return 结果
     */
    boolean support(String grantType, String accountType);

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return NumberUtil.Zero;
    }

    /**
     * 参数校验
     *
     * @param request 请求体
     */
    default void checkParams(HttpServletRequest request) {
    }

    /**
     * 构建登录验证信息体
     *
     * @param reqParameters 请求参数
     * @return 登录验证信息体
     */
    UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

    /**
     * 登录验证
     *
     * @param principal 登录信息
     * @return 用户信息
     */
    UserDetails loadUser(Object principal);

}

