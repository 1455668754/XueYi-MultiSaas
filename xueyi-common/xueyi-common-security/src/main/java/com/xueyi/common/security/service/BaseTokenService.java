package com.xueyi.common.security.service;

import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.MapUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.ip.IpUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.Source;
import com.xueyi.system.api.model.base.BaseLoginUser;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 默认token验证处理
 *
 * @author xueyi
 */
public abstract class BaseTokenService<User, LoginUser extends BaseLoginUser<User>> {

    @Autowired
    protected RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    protected abstract String getAccessToken();

    protected long getAccessExpireTime() {
        return CacheConstants.EXPIRATION;
    }

    protected long getRefreshExpireTime() {
        return CacheConstants.EXPIRATION;
    }

    protected Long getMillisMinuteTen() {
        return CacheConstants.REFRESH_TIME * MILLIS_MINUTE;
    }

    public Map<String, Object> createCache(OAuth2Authorization authorization) {
        return null;
    }

    /**
     * 创建令牌
     *
     * @param loginUser 登录信息
     * @return JWT令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        Map<String, Object> claimsMap = new HashMap<>();

        claimsMap.put(SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode(), loginUser.getAccessToken());
        claimsMap.put(SecurityConstants.BaseSecurity.REFRESH_TOKEN.getCode(), loginUser.getRefreshToken());
        claimsMap.put(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode(), loginUser.getEnterpriseId());
        claimsMap.put(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), loginUser.getEnterpriseName());
        claimsMap.put(SecurityConstants.BaseSecurity.IS_LESSOR.getCode(), loginUser.getIsLessor());
        claimsMap.put(SecurityConstants.BaseSecurity.USER_ID.getCode(), loginUser.getUserId());
        claimsMap.put(SecurityConstants.BaseSecurity.USER_NAME.getCode(), loginUser.getUserName());
        claimsMap.put(SecurityConstants.BaseSecurity.NICK_NAME.getCode(), loginUser.getNickName());
        claimsMap.put(SecurityConstants.BaseSecurity.USER_TYPE.getCode(), loginUser.getUserType());
        claimsMap.put(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), loginUser.getSourceName());
        claimsMap.put(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), loginUser.getAccountType().getCode());

        claimsMap.put(SecurityConstants.BaseSecurity.USER_KEY.getCode(), loginUser.getAccessToken());

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtil.createToken(claimsMap));
        rspMap.put("expires_in", getRefreshExpireTime());
        return rspMap;
    }

    /**
     * 创建令牌
     *
     * @param loginUser 登录信息
     * @param loginMap  缓存存储信息
     */
    protected Map<String, Object> createToken(LoginUser loginUser, Map<String, Object> loginMap) {
        if (MapUtil.isNull(loginMap))
            loginMap = new HashMap<>();

        loginUser.setEnterpriseId(loginUser.getEnterprise().getId());
        loginUser.setEnterpriseName(loginUser.getEnterprise().getName());
        loginUser.setUserId(loginUser.getUserId());
        loginUser.setUserName(loginUser.getUserName());
        loginUser.setNickName(loginUser.getNickName());
        loginUser.setSourceName(loginUser.getSource().getMaster());
        loginUser.setIpaddr(IpUtil.getIpAddr(ServletUtil.getRequest()));

        loginUser.setLoginTime(System.currentTimeMillis());

        // 根据uuid将loginUser缓存
        loginMap.put(SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode(), loginUser.getAccessToken());
        loginMap.put(SecurityConstants.BaseSecurity.REFRESH_TOKEN.getCode(), loginUser.getRefreshToken());
        loginMap.put(SecurityConstants.BaseSecurity.ENTERPRISE.getCode(), loginUser.getEnterprise());
        loginMap.put(SecurityConstants.BaseSecurity.USER.getCode(), loginUser.getUser());
        loginMap.put(SecurityConstants.BaseSecurity.SOURCE.getCode(), loginUser.getSource());
        loginMap.put(SecurityConstants.BaseSecurity.EXPIRE_TIME.getCode(), getExpireTime(loginUser.getLoginTime()));
        loginMap.put(SecurityConstants.BaseSecurity.LOGIN_USER.getCode(), loginUser);
        loginMap.put(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), loginUser.getAccountType().getCode());

        return loginMap;
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    public SysEnterpriseDto getEnterprise() {
        return getEnterprise(ServletUtil.getRequest());
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    public SysEnterpriseDto getEnterprise(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getEnterprise(token);
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    public SysEnterpriseDto getEnterprise(String token) {
        try {
            if (StrUtil.isNotEmpty(token)) {
                String userKey = JwtUtil.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.BaseSecurity.ENTERPRISE.getCode());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public User getUser() {
        return getUser(ServletUtil.getRequest());
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public User getUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getUser(token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public User getUser(String token) {
        try {
            if (StrUtil.isNotEmpty(token)) {
                String userKey = JwtUtil.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.BaseSecurity.USER.getCode());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtil.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        try {
            if (StrUtil.isNotEmpty(token)) {
                String userKey = JwtUtil.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.BaseSecurity.LOGIN_USER.getCode());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (ObjectUtil.isNotNull(loginUser)) {
            if (StrUtil.isNotEmpty(loginUser.getAccessToken())) {
                redisService.setCacheMapValue(loginUser.getAccessToken(), SecurityConstants.BaseSecurity.ENTERPRISE.getCode(), loginUser.getEnterprise());
                redisService.setCacheMapValue(loginUser.getAccessToken(), SecurityConstants.BaseSecurity.USER.getCode(), loginUser.getUser());
                redisService.setCacheMapValue(loginUser.getAccessToken(), SecurityConstants.BaseSecurity.LOGIN_USER.getCode(), loginUser);
            }
            if (StrUtil.isNotEmpty(loginUser.getRefreshToken())) {
                redisService.setCacheMapValue(loginUser.getRefreshToken(), SecurityConstants.BaseSecurity.ENTERPRISE.getCode(), loginUser.getEnterprise());
                redisService.setCacheMapValue(loginUser.getRefreshToken(), SecurityConstants.BaseSecurity.USER.getCode(), loginUser.getUser());
                redisService.setCacheMapValue(loginUser.getRefreshToken(), SecurityConstants.BaseSecurity.LOGIN_USER.getCode(), loginUser);
            }
        }
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    public Source getSource() {
        return getSource(ServletUtil.getRequest());
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    public Source getSource(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getSource(token);
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    public Source getSource(String token) {
        try {
            if (StrUtil.isNotEmpty(token)) {
                String userKey = JwtUtil.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.BaseSecurity.SOURCE.getCode());
            }
        } catch (Exception ignored) {
        }
        return null;
    }


    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    public Long getExpireTime() {
        return getExpireTime(ServletUtil.getRequest());
    }

    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    public Long getExpireTime(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getExpireTime(token);
    }

    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    public Long getExpireTime(String token) {
        try {
            if (StrUtil.isNotEmpty(token)) {
                String userKey = JwtUtil.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.BaseSecurity.EXPIRE_TIME.getCode());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 删除用户缓存信息
     */
    public void delLogin(String token) {
        if (StrUtil.isNotEmpty(token)) {
            String userKey = JwtUtil.getUserKey(token);
            redisService.deleteObject(getTokenKey(userKey));
        }
    }

    /**
     * 验证令牌有效期，自动刷新缓存
     *
     * @param token token
     */
    public void verifyToken(String token) {
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
    public void refreshToken(HttpServletRequest request) {
        refreshToken(SecurityUtils.getToken(request));
    }

    /**
     * 刷新令牌有效期
     *
     * @param token token
     */
    public void refreshToken(String token) {
        long loginTime = System.currentTimeMillis();
        String userKey = getTokenKey(token);
        redisService.setCacheMapValue(userKey, SecurityConstants.BaseSecurity.EXPIRE_TIME.getCode(), getExpireTime(loginTime));
        redisService.expire(userKey, getRefreshExpireTime(), TimeUnit.MINUTES);
    }

    /**
     * 获取TokenKey
     *
     * @param token token
     * @return tokenKey
     */
    protected String getTokenKey(String token) {
        return getAccessToken() + token;
    }

    /**
     * 获取UserKey
     *
     * @return UserKey
     */
    protected String getUserKey() {
        String token = SecurityUtils.getToken(Objects.requireNonNull(ServletUtil.getRequest()));
        if (StrUtil.isNotEmpty(token)) {
            return JwtUtil.getUserKey(token);
        }
        return null;
    }

    /**
     * 获取过期时间
     *
     * @param loginTime 登录时间
     * @return 过期时间
     */
    protected long getExpireTime(long loginTime) {
        return loginTime + getRefreshExpireTime() * MILLIS_MINUTE;
    }

}
