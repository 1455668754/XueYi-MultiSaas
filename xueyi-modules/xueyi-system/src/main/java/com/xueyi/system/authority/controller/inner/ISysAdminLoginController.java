package com.xueyi.system.authority.controller.inner;

import com.xueyi.common.cache.utils.SourceUtil;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.model.SysSource;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.authority.service.ISysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 管理端登录 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/login/admin")
public class ISysAdminLoginController extends BasisController {

    @Autowired
    private ISysLoginService loginService;

    /**
     * 获取登录信息
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping
    public R<LoginUser> getLoginInfoInner(@RequestParam String enterpriseName, @RequestParam String userName, @RequestParam String password) {
        SysEnterpriseDto enterprise = loginService.loginByEnterpriseName(enterpriseName);
        // 不存在直接返回空数据 | 与网络调用错误区分
        if (ObjectUtil.isNull(enterprise))
            return R.ok(null, "企业账号不存在");
        SecurityContextHolder.setEnterpriseId(enterprise.getId().toString());
        SecurityContextHolder.setIsLessor(enterprise.getIsLessor());
        SysSource source = SourceUtil.getSourceCache(enterprise.getStrategyId());
        // 不存在直接返回空数据 | 与网络调用错误区分
        if (ObjectUtil.isNull(source))
            return R.ok(null, "数据源不存在");
        SecurityContextHolder.setSourceName(source.getMaster());
        SysUserDto user = loginService.loginByUser(userName, password);
        // 不存在直接返回空数据 | 与网络调用错误区分
        if (ObjectUtil.isNull(user))
            return R.ok(null, "用户账号不存在");
        SecurityContextHolder.setUserType(user.getUserType());
        // 角色权限标识
        Set<String> roles = loginService.getRolePermission(user.getRoles(), user.getUserType());
        // 角色Id集合
        Set<Long> roleIds = CollUtil.isNotEmpty(user.getRoles())
                ? user.getRoles().stream().map(SysRoleDto::getId).collect(Collectors.toSet())
                : new HashSet<>();

        // 菜单权限标识
        Set<String> permissions = loginService.getMenuPermission(roleIds, user.getUserType());

        // 权限范围
        DataScope dataScope = loginService.getDataScope(user.getRoles(), user);
        dataScope.setRoles(roles);
        dataScope.setRoleIds(roleIds);
        dataScope.setPermissions(permissions);

        // 路由路径集合
        Map<String, String> routeMap = loginService.getMenuRouteMap(roleIds, user.getUserType());
        LoginUser loginUser = new LoginUser();
        loginUser.initEnterprise(enterprise);
        loginUser.initSource(source);
        loginUser.setUser(user);
        loginUser.setUserId(user.getId());
        loginUser.setUserName(user.getUserName());
        loginUser.setUserType(user.getUserType());
        loginUser.setPassword(user.getPassword());
        loginUser.setScope(dataScope);
        loginUser.setRouteURL(routeMap);
        return R.ok(loginUser);
    }
}
