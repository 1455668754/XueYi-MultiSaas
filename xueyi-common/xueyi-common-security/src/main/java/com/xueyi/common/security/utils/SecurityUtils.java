package com.xueyi.common.security.utils;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.common.core.web.model.SysEnterprise;
import com.xueyi.common.core.web.model.SysSource;
import com.xueyi.common.security.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.security.Principal;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 默认权限获取工具类
 *
 * @author xueyi
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class SecurityUtils {

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
     * 获取用户昵称
     */
    public static String getNickName() {
        return SecurityContextHolder.getNickName();
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
    public static SecurityConstants.AccountType getAccountType() {
        return SecurityConstants.AccountType.getByCodeElseNull(SecurityContextHolder.getAccountType());
    }

    /**
     * 获取企业信息
     */
    public static SysEnterprise getEnterprise() {
        return getTokenService().getEnterprise();
    }

    /**
     * 获取用户信息
     */
    public static <User> User getUser() {
        return (User) getTokenService().getUser();
    }

    /**
     * 获取源策略信息
     */
    public static SysSource getSource() {
        return getTokenService().getSource();
    }

    /**
     * 获取登录用户信息
     */
    public static <LoginUser extends BaseLoginUser<?>> LoginUser getLoginUser() {
        return (LoginUser) getTokenService().getLoginUser();
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(Objects.requireNonNull(ServletUtil.getRequest()));
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识 | 优先识别补充Token
        String token = request.getHeader(TokenConstants.SUPPLY_AUTHORIZATION);
        if (StrUtil.isBlank(token)) {
            token = request.getHeader(TokenConstants.AUTHENTICATION);
        }
        return replaceTokenPrefix(token);
    }

    /**
     * 获取用户信息
     *
     * @param authorization 认证信息
     * @return 用户信息
     */
    public static <LoginUser> LoginUser getLoginUser(OAuth2Authorization authorization) {
        return Optional.ofNullable(authorization)
                .map(item -> (UsernamePasswordAuthenticationToken) authorization.getAttribute(Principal.class.getName()))
                .map(item -> (LoginUser) item.getPrincipal())
                .orElseThrow(() -> new NullPointerException("authorization principal cannot be null"));
    }

    /**
     * 获取账户类型Token控制器
     *
     * @return Token控制器
     */
    public static ITokenService getTokenService() {
        return getTokenService(getAccountTypeStr());
    }

    /**
     * 获取账户类型Token控制器
     *
     * @param accountType 账户类型
     * @return Token控制器
     */
    public static ITokenService getTokenService(String accountType) {
        Map<String, ITokenService> tokenServiceMap = SpringUtil.getBeansOfType(ITokenService.class);
        Optional<ITokenService> optional = tokenServiceMap.values().stream()
                .filter(service -> service.support(accountType))
                .max(Comparator.comparingInt(Ordered::getOrder));
        if (optional.isEmpty()) {
            throw new NullPointerException("tokenService error , non-existent");
        }
        return optional.get();
    }

    /**
     * 校验是否已登录
     *
     * @return 结果
     */
    public static boolean hasLogin() {
        try {
            String accountType = getAccountTypeStr();
            if (StrUtil.isBlank(accountType)) {
                accountType = ServletUtil.getHeader(Objects.requireNonNull(ServletUtil.getRequest()), SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
            }
            if (StrUtil.isBlank(accountType)) {
                return false;
            }
            return getTokenService(accountType).hasLogin();
        } catch (Exception e) {
            return false;
        }
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
     * 是否为超管租户
     */
    public static boolean isAdminTenant() {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), getIsLessor());
    }

    /**
     * 是否不为超管租户
     */
    public static boolean isNotAdminTenant() {
        return !isAdminTenant();
    }

    /**
     * 是否为超管用户
     */
    public static boolean isAdminUser() {
        return StrUtil.equals(AuthorityConstants.UserType.ADMIN.getCode(), getUserType());
    }

    /**
     * 是否不为超管用户
     */
    public static boolean isNotAdminUser() {
        return !isAdminUser();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
