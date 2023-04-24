package com.xueyi.common.security.service;

import com.xueyi.common.cache.constants.CacheConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * token控制器 | 后台账户
 *
 * @author xueyi
 */
@Component
public class TokenUserService implements ITokenService<SysUserDto, LoginUser> {

    @Getter
    @Autowired
    private RedisService redisService;

    /**
     * 判断账户类型
     *
     * @param accountType 账户类型
     * @return 结果
     */
    @Override
    public boolean support(String accountType) {
        return StrUtil.equals(SecurityConstants.AccountType.ADMIN.getCode(), accountType);
    }

    /**
     * 构建缓存路径
     *
     * @param type         密钥类型
     * @param enterpriseId 企业Id
     * @param tokenValue   token值
     * @return 缓存路径
     */
    @Override
    public String getTokenAddress(String type, Long enterpriseId, String tokenValue) {
        if (ObjectUtil.isNull(enterpriseId) || StrUtil.isBlank(tokenValue)) {
            throw new NullPointerException("enterpriseId or tokenValue has empty");
        }
        return StrUtil.format("{}:{}:{}:{}:{}", CacheConstants.AUTHORIZATION, CacheConstants.LoginTokenType.ADMIN.getCode(), enterpriseId, type, tokenValue);
    }

    /**
     * 构建令牌缓存 | 自定义构建
     *
     * @param loginUser 用户登录信息
     * @param loginMap  缓存存储信息
     * @return 令牌缓存
     */
    @Override
    public Map<String, Object> buildTokenCache(LoginUser loginUser, Map<String, Object> loginMap) {
        loginMap.put(SecurityConstants.AdminSecurity.DATA_SCOPE.getCode(), loginUser.getDataScope());
        loginMap.put(SecurityConstants.AdminSecurity.ROUTE_URL.getCode(), loginUser.getRouteURL());
        loginUser.initRouteURL();
        return loginMap;
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
        String token = SecurityUserUtils.getToken(request);
        return getDataScope(token);
    }

    /**
     * 获取数据权限信息
     *
     * @return 数据权限信息
     */
    public DataScope getDataScope(String token) {
        try {
            if (StrUtil.isNotBlank(token)) {
                return redisService.getCacheMapValue(JwtUtil.getUserKey(token), SecurityConstants.AdminSecurity.DATA_SCOPE.getCode());
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
            return redisService.getCacheMapValue(getUserKey(), SecurityConstants.AdminSecurity.MODULE_ROUTE.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置模块路由列表信息
     */
    public void setModuleRoute(Object moduleRoute) {
        redisService.setCacheMapValue(getUserKey(), SecurityConstants.AdminSecurity.MODULE_ROUTE.getCode(), moduleRoute);
    }

    /**
     * 获取菜单路由列表信息
     *
     * @return 菜单路由列表信息
     */
    public Map<String, Object> getMenuRoute() {
        try {
            return redisService.getCacheMapValue(getUserKey(), SecurityConstants.AdminSecurity.MENU_ROUTE.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置菜单路由列表信息
     */
    public void setMenuRoute(Map<String, Object> menuRoute) {
        redisService.setCacheMapValue(getUserKey(), SecurityConstants.AdminSecurity.MENU_ROUTE.getCode(), menuRoute);
    }

    /**
     * 获取路由路径映射列表信息
     *
     * @return 路由路径映射列表信息
     */
    public Map<String, String> getRouteURL() {
        try {
            return redisService.getCacheMapValue(getUserKey(), SecurityConstants.AdminSecurity.ROUTE_URL.getCode());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置路由路径映射列表信息
     */
    public void setRouteURL(Map<String, String> routeURL) {
        redisService.setCacheMapValue(getUserKey(), SecurityConstants.AdminSecurity.ROUTE_URL.getCode(), routeURL);
    }
}
