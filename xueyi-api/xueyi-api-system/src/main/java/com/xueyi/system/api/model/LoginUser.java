package com.xueyi.system.api.model;

import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.system.api.model.base.BaseLoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;

import java.util.Map;

/**
 * 管理端 - 用户信息
 *
 * @author xueyi
 */
public class LoginUser  extends BaseLoginUser<SysUserDto> {

    private static final long serialVersionUID = 1L;

    /** 数据权限 */
    private DataScope scope;

    /** 路由路径映射列表 */
    private Map<String, String> routeURL;

    public DataScope getScope() {
        return scope;
    }

    public void setScope(DataScope scope) {
        this.scope = scope;
    }

    public Map<String, String> getRouteURL() {
        return routeURL;
    }

    public void setRouteURL(Map<String, String> routeURL) {
        this.routeURL = routeURL;
    }

    /** 初始化权限范围 */
    public DataScope getDataScope() {
        scope.setEnterpriseId(enterpriseId);
        scope.setUserId(userId);
        scope.setIsLessor(isLessor);
        scope.setUserType(userType);
        return scope;
    }

    /** 初始化路由路径映射列表 */
    public void initRouteURL() {
        routeURL = null;
    }

    @Override
    public TenantConstants.AccountType getAccountType() {
        return accountType;
    }

    @Override
    public void setAccountType(TenantConstants.AccountType accountType) {
        this.accountType = accountType;
    }
}
