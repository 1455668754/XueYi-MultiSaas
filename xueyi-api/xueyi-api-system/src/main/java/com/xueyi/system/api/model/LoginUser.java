package com.xueyi.system.api.model;

import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Map;

/**
 * 管理端 - 用户信息
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUser extends BaseLoginUser<SysUserDto> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 数据权限 */
    private DataScope scope;

    /** 路由路径映射列表 */
    private Map<String, String> routeURL;

    /** 账户类型 */
    private TenantConstants.AccountType accountType = TenantConstants.AccountType.ADMIN;

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

}
