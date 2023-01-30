package com.xueyi.common.security.utils.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 默认权限获取工具类
 *
 * @author xueyi
 */
public class BaseSecurityUtils {

    /**
     * 获取企业Id
     */
    public static Long getEnterpriseId() {
        return SecurityContextHolder.getEnterpriseId();
    }

    /**
     * 获取企业名称
     */
    public static String getEnterpriseName() {
        return SecurityContextHolder.getEnterpriseName();
    }

    /**
     * 获取租户权限标识
     */
    public static String getIsLessor() {
        return SecurityContextHolder.getIsLessor();
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUserName() {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType() {
        return SecurityContextHolder.getUserType();
    }

    /**
     * 获取租户策略源
     */
    public static String getSourceName() {
        return SecurityContextHolder.getSourceName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取账户类型
     */
    public static String getAccountTypeStr() {
        return SecurityContextHolder.getAccountType();
    }

    /**
     * 获取账户类型 枚举
     */
    public static TenantConstants.AccountType getAccountType() {
        return TenantConstants.AccountType.getByCodeElseNull(SecurityContextHolder.getAccountType());
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtil.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        return StrUtil.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX) ? token.replaceFirst(TokenConstants.PREFIX, StrUtil.EMPTY) : token;
    }

    /**
     * 是否为空租户信息
     */
    public static boolean isEmptyTenant() {
        return ObjectUtil.equals(SecurityConstants.EMPTY_TENANT_ID, getEnterpriseId());
    }

    /**
     * 是否不为空租户信息
     */
    public static boolean isNotEmptyTenant() {
        return !isEmptyTenant();
    }

    /**
     * 是否为白名单
     */
    public static boolean isAllowList(HttpServletRequest request) {
        String allowStr = request.getHeader(SecurityConstants.BaseSecurity.ALLOW_LIST.getCode());
        return StrUtil.isNotEmpty(allowStr) && StrUtil.equals(BaseConstants.Whether.YES.getCode(), allowStr);
    }
}
