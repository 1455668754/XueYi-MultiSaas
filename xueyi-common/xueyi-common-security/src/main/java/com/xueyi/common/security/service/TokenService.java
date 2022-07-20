package com.xueyi.common.security.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.JwtUtils;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.ip.IpUtils;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.source.domain.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author xueyi
 */
@Component
public class TokenService {

    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {

        Long enterpriseId = loginUser.getEnterprise().getId();
        String enterpriseName = loginUser.getEnterprise().getName();
        String isLessor = loginUser.getEnterprise().getIsLessor();
        Long userId = loginUser.getUser().getId();
        String userName = loginUser.getUser().getUserName();
        String nickName = loginUser.getUser().getNickName();
        String userType = loginUser.getUser().getUserType();
        String sourceName = loginUser.getSource().getMaster();

        String token = enterpriseId + StrUtil.COLON + IdUtil.fastUUID();

        loginUser.setToken(token);
        loginUser.setEnterpriseId(enterpriseId);
        loginUser.setEnterpriseName(enterpriseName);
        loginUser.setIsLessor(isLessor);
        loginUser.setUserId(userId);
        loginUser.setUserName(userName);
        loginUser.setNickName(nickName);
        loginUser.setUserType(userType);
        loginUser.setSourceName(sourceName);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));

        long loginTime = System.currentTimeMillis();
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put(SecurityConstants.TOKEN, loginUser.getToken());
        loginMap.put(SecurityConstants.ENTERPRISE, loginUser.getEnterprise());
        loginMap.put(SecurityConstants.USER, loginUser.getUser());
        loginMap.put(SecurityConstants.SOURCE, loginUser.getSource());
        loginMap.put(SecurityConstants.DATA_SCOPE, loginUser.getDataScope());
        loginMap.put(SecurityConstants.ROUTE_URL, loginUser.getRouteURL());
        loginMap.put(SecurityConstants.LOGIN_TIME, loginTime);
        loginMap.put(SecurityConstants.EXPIRE_TIME, loginTime + expireTime * MILLIS_MINUTE);
        loginUser.initRouteURL();
        loginMap.put(SecurityConstants.LOGIN_USER, loginUser);
        redisService.setCacheMap(userKey, loginMap);
        redisService.expire(userKey, expireTime, TimeUnit.MINUTES);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.ENTERPRISE_ID, enterpriseId);
        claimsMap.put(SecurityConstants.ENTERPRISE_NAME, enterpriseName);
        claimsMap.put(SecurityConstants.IS_LESSOR, isLessor);
        claimsMap.put(SecurityConstants.USER_ID, userId);
        claimsMap.put(SecurityConstants.USER_NAME, userName);
        claimsMap.put(SecurityConstants.NICK_NAME, nickName);
        claimsMap.put(SecurityConstants.USER_TYPE, userType);
        claimsMap.put(SecurityConstants.SOURCE_NAME, sourceName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取企业信息
     *
     * @return 企业信息
     */
    public SysEnterpriseDto getEnterprise() {
        return getEnterprise(ServletUtils.getRequest());
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
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.ENTERPRISE);
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
    public SysUserDto getUser() {
        return getUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public SysUserDto getUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getUser(token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public SysUserDto getUser(String token) {
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.USER);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取源策略信息
     *
     * @return 源策略信息
     */
    public Source getSource() {
        return getSource(ServletUtils.getRequest());
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
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.SOURCE);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取数据权限信息
     *
     * @return 数据权限信息
     */
    public DataScope getDataScope() {
        return getDataScope(ServletUtils.getRequest());
    }

    /**
     * 获取数据权限信息
     *
     * @return 数据权限信息
     */
    public DataScope getDataScope(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getDataScope(token);
    }

    /**
     * 获取数据权限信息
     *
     * @return 数据权限信息
     */
    public DataScope getDataScope(String token) {
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.DATA_SCOPE);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取模块路由列表信息
     *
     * @return 模块路由列表信息
     */
    public Object getModuleRoute() {
        try {
            return redisService.getCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.MODULE_ROUTE);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置模块路由列表信息
     */
    public void setModuleRoute(Object moduleRoute) {
        redisService.setCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.MODULE_ROUTE, moduleRoute);
    }

    /**
     * 获取菜单路由列表信息
     *
     * @return 菜单路由列表信息
     */
    public Map<String, Object> getMenuRoute() {
        try {
            return redisService.getCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.MENU_ROUTE);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置菜单路由列表信息
     */
    public void setMenuRoute(Map<String, Object> menuRoute) {
        redisService.setCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.MENU_ROUTE, menuRoute);
    }

    /**
     * 获取路由路径映射列表信息
     *
     * @return 路由路径映射列表信息
     */
    public Map<String, String> getRouteURL() {
        try {
            return redisService.getCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.ROUTE_URL);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置路由路径映射列表信息
     */
    public void setRouteURL(Map<String, String> routeURL) {
        redisService.setCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.ROUTE_URL, routeURL);
    }

    /**
     * 获取过期时间信息
     *
     * @return 过期时间信息
     */
    public Long getExpireTime() {
        return getExpireTime(ServletUtils.getRequest());
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
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.EXPIRE_TIME);
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
        return getLoginUser(ServletUtils.getRequest());
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
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.LOGIN_USER);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            redisService.setCacheMapValue(getTokenKey(loginUser.getToken()), SecurityConstants.ENTERPRISE, loginUser.getEnterprise());
            redisService.setCacheMapValue(getTokenKey(loginUser.getToken()), SecurityConstants.USER, loginUser.getUser());
            redisService.setCacheMapValue(getTokenKey(loginUser.getToken()), SecurityConstants.LOGIN_USER, loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLogin(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userKey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param token token
     */
    public void verifyToken(String token) {
        long expireTime = getExpireTime(token);
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
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
        Long loginTime = System.currentTimeMillis();
        String userKey = getTokenKey(token);
        redisService.setCacheMapValue(userKey, SecurityConstants.LOGIN_TIME, loginTime);
        redisService.setCacheMapValue(userKey, SecurityConstants.EXPIRE_TIME, loginTime);
        redisService.expire(userKey, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取TokenKey
     *
     * @param token token
     * @return tokenKey
     */
    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }

    /**
     * 获取UserKey
     *
     * @return UserKey
     */
    private String getUserKey() {
        String token = SecurityUtils.getToken(Objects.requireNonNull(ServletUtils.getRequest()));
        if (StringUtils.isNotEmpty(token)) {
            return JwtUtils.getUserKey(token);
        }
        return null;
    }
}
