package com.xueyi.common.security.service;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.MapUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.ip.IpUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.common.core.web.model.SysSource;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.xueyi.common.core.constant.basic.CacheConstants.EXPIRATION;
import static com.xueyi.common.core.constant.basic.CacheConstants.REFRESH_TIME;

/**
 * token控制器
 *
 * @author xueyi
 */
@SuppressWarnings(value = {"unchecked"})
public interface ITokenService<User, LoginUser extends BaseLoginUser<User>> extends Ordered {

    RedisService getRedisService();

    /**
     * 判断账户类型
     *
     * @param accountType 账户类型
     * @return 结果
     */
    boolean support(String accountType);

    /**
     * 构建令牌缓存路径
     *
     * @param type         密钥类型
     * @param enterpriseId 企业Id
     * @param tokenValue   token值
     * @return 令牌缓存路径
     */
    String getTokenAddress(String type, Long enterpriseId, String tokenValue);

    /**
     * 排序值 | 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return NumberUtil.Zero;
    }

    default long getAccessExpireTime() {
        return REFRESH_TIME;
    }

    default long getRefreshExpireTime() {
        return EXPIRATION;
    }

    default Long getMillisMinuteTen() {
        return REFRESH_TIME;
    }

    /**
     * 创建令牌
     *
     * @param loginUser 登录信息
     * @return JWT令牌
     */
    default Map<String, Object> createToken(LoginUser loginUser) {
        Map<String, Object> claimsMap = buildToken(loginUser, new HashMap<>());
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtil.createToken(claimsMap));
        rspMap.put("expires_in", getRefreshExpireTime());
        return rspMap;
    }

    /**
     * 创建令牌缓存
     *
     * @param authorization 用户认证信息
     */
    default void createTokenCache(OAuth2Authorization authorization) {
        Map<String, Object> loginMap = buildTokenCache(authorization, new HashMap<>());
        createTokenCache(authorization, (LoginUser) loginMap.get(SecurityConstants.BaseSecurity.USER_INFO.getCode()), loginMap);
    }

    /**
     * 创建令牌缓存 | 定义缓存
     *
     * @param authorization 用户认证信息
     * @param loginUser     用户认证信息
     * @param loginMap      缓存存储信息
     */
    default void createTokenCache(OAuth2Authorization authorization, LoginUser loginUser, Map<String, Object> loginMap) {
        if (ObjectUtil.isNotNull(loginUser.getAccessToken()))
            getRedisService().setCacheObject(loginUser.getAccessToken(), authorization, getAccessExpireTime(), TimeUnit.MINUTES, RedisSerializer.java());
        if (ObjectUtil.isNotNull(loginUser.getRefreshToken()))
            getRedisService().setCacheMap(loginUser.getRefreshToken(), loginMap, getRefreshExpireTime(), TimeUnit.MINUTES);
        if (ObjectUtil.isNotNull(loginUser.getStateToken()))
            getRedisService().setCacheObject(loginUser.getStateToken(), authorization, getAccessExpireTime(), TimeUnit.MINUTES, RedisSerializer.java());
        if (ObjectUtil.isNotNull(loginUser.getCodeToken()))
            getRedisService().setCacheObject(loginUser.getCodeToken(), authorization, getAccessExpireTime(), TimeUnit.MINUTES, RedisSerializer.java());
    }

    /**
     * 删除令牌缓存
     *
     * @param loginUser 用户认证信息
     */
    default void removeTokenCache(LoginUser loginUser) {
        List<String> keys = new ArrayList<>();
        if (ObjectUtil.isNotNull(loginUser.getAccessToken()))
            keys.add(loginUser.getAccessToken());
        if (ObjectUtil.isNotNull(loginUser.getRefreshToken()))
            keys.add(loginUser.getRefreshToken());
        if (ObjectUtil.isNotNull(loginUser.getStateToken()))
            keys.add(loginUser.getStateToken());
        if (ObjectUtil.isNotNull(loginUser.getCodeToken()))
            keys.add(loginUser.getCodeToken());
        if (CollUtil.isNotEmpty(keys))
            getRedisService().deleteObject(keys);
    }

    /**
     * 构建令牌
     *
     * @param loginUser 登录信息
     * @param claimsMap 令牌存储信息
     * @return JWT令牌
     */
    default Map<String, Object> buildToken(LoginUser loginUser, Map<String, Object> claimsMap) {
        if (MapUtil.isNull(claimsMap))
            claimsMap = new HashMap<>();

        claimsMap.put(SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode(), TokenConstants.PREFIX + loginUser.getAccessToken());
        claimsMap.put(SecurityConstants.BaseSecurity.REFRESH_TOKEN.getCode(), TokenConstants.PREFIX + loginUser.getRefreshToken());
        claimsMap.put(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode(), loginUser.getEnterpriseId());
        claimsMap.put(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), loginUser.getEnterpriseName());
        claimsMap.put(SecurityConstants.BaseSecurity.IS_LESSOR.getCode(), loginUser.getIsLessor());
        claimsMap.put(SecurityConstants.BaseSecurity.USER_ID.getCode(), loginUser.getUserId());
        claimsMap.put(SecurityConstants.BaseSecurity.USER_NAME.getCode(), loginUser.getUserName());
        claimsMap.put(SecurityConstants.BaseSecurity.NICK_NAME.getCode(), loginUser.getNickName());
        claimsMap.put(SecurityConstants.BaseSecurity.USER_TYPE.getCode(), loginUser.getUserType());
        claimsMap.put(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), loginUser.getSourceName());
        claimsMap.put(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), loginUser.getAccountType().getCode());

        return claimsMap;
    }

    /**
     * 构建令牌缓存
     *
     * @param authorization 用户认证信息
     * @param loginMap      缓存存储信息
     * @return 令牌缓存
     */
    default Map<String, Object> buildTokenCache(OAuth2Authorization authorization, Map<String, Object> loginMap) {
        LoginUser loginUser = Optional.ofNullable(authorization)
                .map(item -> (UsernamePasswordAuthenticationToken) authorization.getAttribute(Principal.class.getName()))
                .map(item -> (LoginUser) item.getPrincipal())
                .orElseThrow(() -> new NullPointerException("authorization principal cannot be null"));

        if (MapUtil.isNull(loginMap))
            loginMap = new HashMap<>();

        loginUser.setIpaddr(IpUtil.getIpAddr(ServletUtil.getRequest()));
        loginUser.setLoginTime(System.currentTimeMillis());

        // 自定义构建
        loginMap = buildTokenCache(loginUser, loginMap);

        // 默认构建
        loginMap.put(SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode(), loginUser.getAccessToken());
        loginMap.put(SecurityConstants.BaseSecurity.REFRESH_TOKEN.getCode(), loginUser.getRefreshToken());
        loginMap.put(SecurityConstants.BaseSecurity.ENTERPRISE.getCode(), loginUser.getEnterprise());
        loginMap.put(SecurityConstants.BaseSecurity.USER.getCode(), loginUser.getUser());
        loginMap.put(SecurityConstants.BaseSecurity.SOURCE.getCode(), loginUser.getSource());
        loginMap.put(SecurityConstants.BaseSecurity.EXPIRE_TIME.getCode(), getExpireTime(loginUser.getLoginTime()));
        loginMap.put(SecurityConstants.BaseSecurity.USER_INFO.getCode(), loginUser);
        loginMap.put(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), loginUser.getAccountType().getCode());

        return loginMap;
    }

    /**
     * 构建令牌缓存 | 自定义构建
     *
     * @param loginUser 用户登录信息
     * @param loginMap  缓存存储信息
     * @return 令牌缓存
     */
    default Map<String, Object> buildTokenCache(LoginUser loginUser, Map<String, Object> loginMap) {
        return loginMap;
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    default SysEnterpriseDto getEnterprise() {
        return getEnterprise(ServletUtil.getRequest());
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    default SysEnterpriseDto getEnterprise(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = BaseSecurityUtils.getToken(request);
        return getEnterprise(token);
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    default SysEnterpriseDto getEnterprise(String token) {
        try {
            if (StrUtil.isNotBlank(token))
                return getRedisService().getCacheMapValue(JwtUtil.getUserKey(token), SecurityConstants.BaseSecurity.ENTERPRISE.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    default User getUser() {
        return getUser(ServletUtil.getRequest());
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    default User getUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = BaseSecurityUtils.getToken(request);
        return getUser(token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    default User getUser(String token) {
        try {
            if (StrUtil.isNotBlank(token))
                return getRedisService().getCacheMapValue(JwtUtil.getUserKey(token), SecurityConstants.BaseSecurity.USER.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    default LoginUser getLoginUser() {
        return getLoginUser(ServletUtil.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    default LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = BaseSecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    default LoginUser getLoginUser(String token) {
        try {
            if (StrUtil.isNotBlank(token))
                return getRedisService().getCacheMapValue(JwtUtil.getUserKey(token), SecurityConstants.BaseSecurity.USER_INFO.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    default void setLoginUser(LoginUser loginUser) {
        if (ObjectUtil.isNotNull(loginUser)) {
            Map<String, Object> loginMap = new HashMap<>();
            loginMap.put(SecurityConstants.BaseSecurity.ENTERPRISE.getCode(), loginUser.getEnterprise());
            loginMap.put(SecurityConstants.BaseSecurity.USER.getCode(), loginUser.getUser());
            loginMap.put(SecurityConstants.BaseSecurity.USER_INFO.getCode(), loginUser);
            if (StrUtil.isNotBlank(loginUser.getRefreshToken()))
                getRedisService().setCacheMap(loginUser.getRefreshToken(), loginMap);
        }
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    default SysSource getSource() {
        return getSource(ServletUtil.getRequest());
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    default SysSource getSource(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = BaseSecurityUtils.getToken(request);
        return getSource(token);
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    default SysSource getSource(String token) {
        try {
            if (StrUtil.isNotBlank(token))
                return getRedisService().getCacheMapValue(JwtUtil.getUserKey(token), SecurityConstants.BaseSecurity.SOURCE.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    default Long getExpireTime() {
        return getExpireTime(ServletUtil.getRequest());
    }

    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    default Long getExpireTime(HttpServletRequest request) {
        // 获取请求携带的令牌
        return getExpireTime(BaseSecurityUtils.getToken(request));
    }

    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    default Long getExpireTime(String token) {
        try {
            if (StrUtil.isNotBlank(token))
                return getRedisService().getCacheMapValue(JwtUtil.getUserKey(token), SecurityConstants.BaseSecurity.EXPIRE_TIME.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 删除用户缓存信息
     */
    default void delLogin(String token) {
        if (StrUtil.isNotBlank(token))
            getRedisService().deleteObject(JwtUtil.getUserKey(token));
    }

    /**
     * 验证令牌有效期，自动刷新缓存
     *
     * @param token token
     */
    default void verifyToken(String token) {
        long expireTime = getExpireTime(token);
        if (ObjectUtil.isNull(expireTime))
            throw new ServiceException("令牌已失效！");
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= getMillisMinuteTen()) {
            refreshToken(token);
        }
    }

    /**
     * 刷新令牌有效期
     */
    default void refreshToken(HttpServletRequest request) {
        refreshToken(BaseSecurityUtils.getToken(request));
    }

    /**
     * 刷新令牌有效期
     *
     * @param token token
     */
    default void refreshToken(String token) {
        long loginTime = System.currentTimeMillis();
        getRedisService().setCacheMapValue(token, SecurityConstants.BaseSecurity.EXPIRE_TIME.getCode(), getExpireTime(loginTime));
        getRedisService().expire(token, getRefreshExpireTime(), TimeUnit.MINUTES);
    }

    /**
     * 获取UserKey
     *
     * @return UserKey
     */
    default String getUserKey() {
        String token = BaseSecurityUtils.getToken(Objects.requireNonNull(ServletUtil.getRequest()));
        return StrUtil.isNotBlank(token) ? JwtUtil.getUserKey(token) : null;
    }

    /**
     * 获取过期时间
     *
     * @param loginTime 登录时间
     * @return 过期时间
     */
    default long getExpireTime(long loginTime) {
        return loginTime + getRefreshExpireTime();
    }

}
