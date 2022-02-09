package com.xueyi.system.authority.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.utils.SourceUtils;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.source.domain.Source;
import com.xueyi.system.authority.service.ISysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/login")
public class SysLoginController extends BasisController {

    @Autowired
    ISysLoginService loginService;

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{enterpriseName}/{userName}/{password}")
    public R<LoginUser> info(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName, @PathVariable("password") String password) {
        SysEnterpriseDto enterprise = loginService.loginByEnterpriseName(enterpriseName);
        if (ObjectUtil.isNull(enterprise)) {
            return R.fail("账号或密码错误，请检查");
        }
        Source source = SourceUtils.getSourceCache(enterprise.getStrategyId());
        SysUserDto user = loginService.loginByUser(userName, password, enterprise.getId(), source.getMaster());
        if (ObjectUtil.isNull(user)) {
            return R.fail("账号或密码错误，请检查");
        }
        // 角色权限标识
        Set<String> roles = loginService.getRolePermission(user.getRoles(), enterprise.getIsLessor(), user.getUserType());
        // 角色Id集合
        Set<Long> roleIds = CollUtil.isNotEmpty(user.getRoles())
                ? user.getRoles().stream().map(SysRoleDto::getId).collect(Collectors.toSet())
                : new HashSet<>();
        // 菜单权限标识
        Set<String> permissions = loginService.getMenuPermission(roleIds, enterprise.getIsLessor(), user.getUserType(), enterprise.getId(), source.getMaster());
        // 路由路径集合
        Map<String, String> routeMap = loginService.getMenuRouteMap(roleIds, enterprise.getIsLessor(), user.getUserType(), enterprise.getId(), source.getMaster());
        LoginUser loginUser = new LoginUser();
        loginUser.setEnterprise(enterprise);
        loginUser.setUser(user);
        loginUser.setRoles(roles);
        loginUser.setRoleIds(roleIds);
        loginUser.setPermissions(permissions);
        loginUser.setRouteMap(routeMap);
        loginUser.setSource(source);
        return R.ok(loginUser);
    }
}
