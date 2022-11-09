package com.xueyi.common.security.service;

import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * token验证处理
 *
 * @author xueyi
 */
@Component
public class TokenService extends BaseTokenService<SysUserDto, LoginUser>{

    @Override
    protected String getAccessToken() {
        return CacheConstants.LoginTokenType.ADMIN.getCode();
    }

    @Override
    protected long getTacitExpireTime() {
        return CacheConstants.EXPIRATION;
    }

    @Override
    protected Long getMillisMinuteTen() {
        return CacheConstants.REFRESH_TIME * MILLIS_MINUTE;
    }

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {

        String isLessor = loginUser.getEnterprise().getIsLessor();
        String userType = loginUser.getUser().getUserType();

        loginUser.setIsLessor(isLessor);
        loginUser.setUserType(userType);

        Map<String, Object> loginMap = new HashMap<>();

        loginMap.put(SecurityConstants.AdminSecurity.DATA_SCOPE.getCode(), loginUser.getDataScope());
        loginMap.put(SecurityConstants.AdminSecurity.ROUTE_URL.getCode(), loginUser.getRouteURL());
        loginUser.initRouteURL();

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();

        return createToken(loginUser, loginMap, claimsMap);
    }

    /**
     * 获取数据权限信息
     *
     * @return 数据权限信息
     */
    public DataScope getDataScope() {
        return getDataScope(ServletUtil.getRequest());
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
            if (StrUtil.isNotEmpty(token)) {
                String userKey = JwtUtil.getUserKey(token);
                return redisService.getCacheMapValue(getTokenKey(userKey), SecurityConstants.AdminSecurity.DATA_SCOPE.getCode());
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
            return redisService.getCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.AdminSecurity.MODULE_ROUTE.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置模块路由列表信息
     */
    public void setModuleRoute(Object moduleRoute) {
        redisService.setCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.AdminSecurity.MODULE_ROUTE.getCode(), moduleRoute);
    }

    /**
     * 获取菜单路由列表信息
     *
     * @return 菜单路由列表信息
     */
    public Map<String, Object> getMenuRoute() {
        try {
            return redisService.getCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.AdminSecurity.MENU_ROUTE.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置菜单路由列表信息
     */
    public void setMenuRoute(Map<String, Object> menuRoute) {
        redisService.setCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.AdminSecurity.MENU_ROUTE.getCode(), menuRoute);
    }

    /**
     * 获取路由路径映射列表信息
     *
     * @return 路由路径映射列表信息
     */
    public Map<String, String> getRouteURL() {
        try {
            return redisService.getCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.AdminSecurity.ROUTE_URL.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置路由路径映射列表信息
     */
    public void setRouteURL(Map<String, String> routeURL) {
        redisService.setCacheMapValue(getTokenKey(getUserKey()), SecurityConstants.AdminSecurity.ROUTE_URL.getCode(), routeURL);
    }
}
