package com.xueyi.system.authority.service.impl;

import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.organize.service.ISysOrganizeService;
import com.xueyi.system.organize.service.ISysPostService;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xueyi.common.core.constant.basic.SecurityConstants.PERMISSION_ADMIN;
import static com.xueyi.common.core.constant.basic.SecurityConstants.ROLE_ADMIN;
import static com.xueyi.common.core.constant.basic.SecurityConstants.ROLE_ADMINISTRATOR;

/**
 * 登录管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysLoginServiceImpl implements ISysLoginService {

    @Autowired
    ISysEnterpriseService enterpriseService;

    @Autowired
    ISysDeptService deptService;

    @Autowired
    ISysPostService postService;

    @Autowired
    ISysUserService userService;

    @Autowired
    ISysModuleService moduleService;

    @Autowired
    ISysMenuService menuService;

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 登录校验 | 根据企业账号查询企业信息
     *
     * @param enterpriseName 企业账号
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto loginByEnterpriseName(String enterpriseName) {
        return enterpriseService.selectByName(enterpriseName);
    }

    /**
     * 登录校验 | 根据用户账号查询用户信息
     *
     * @param userName 用户账号
     * @param password 密码
     * @return 用户对象
     */
    @Override
    public SysUserDto loginByUser(String userName, String password) {
        return userService.userLogin(userName, password);
    }

    /**
     * 登录校验 | 获取角色数据权限
     *
     * @param roleList 角色信息集合
     * @param isLessor 租户标识
     * @param userType 用户标识
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(List<SysRoleDto> roleList, String isLessor, String userType) {
        Set<String> roles = new HashSet<>();
        // 租管租户拥有租管标识权限
        if (SysEnterpriseDto.isLessor(isLessor)) {
            roles.add(ROLE_ADMINISTRATOR);
        }
        // 超管用户拥有超管标识权限
        if (SysUserDto.isAdmin(userType)) {
            roles.add(ROLE_ADMIN);
        } else {
            roles.addAll(roleList.stream().map(SysRoleDto::getRoleKey).filter(StrUtil::isNotBlank).collect(Collectors.toSet()));
        }
        return roles;
    }

    /**
     * 登录校验 | 获取权限模块列表
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 模块信息对象集合
     */
    @Override
    public List<SysModuleDto> getModuleList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType) {
        return moduleService.selectEnterpriseList(authGroupIds, roleIds, isLessor, userType);
    }

    /**
     * 登录校验 | 获取权限菜单列表
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 菜单信息对象集合
     */
    @Override
    public List<SysMenuDto> getMenuList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType) {
        return menuService.selectEnterpriseList(authGroupIds, roleIds, isLessor, userType);
    }

    /**
     * 登录校验 | 获取菜单数据权限
     *
     * @param menuList 菜单信息对象集合
     * @param isLessor 租户标识
     * @param userType 用户标识
     * @return 菜单权限信息集合
     */
    @Override
    public Set<String> getMenuPermission(List<SysMenuDto> menuList, String isLessor, String userType) {
        Set<String> perms = new HashSet<>();
        // 租管租户的超管用户拥有所有权限
        if (SysEnterpriseDto.isLessor(isLessor) && SysUserDto.isAdmin(userType)) {
            perms.add(PERMISSION_ADMIN);
        } else {
            // 菜单组合权限表示集合
            perms.addAll(menuList.stream().map(SysMenuDto::getPerms).filter(StrUtil::isNotBlank).collect(Collectors.toSet()));
        }
        return perms;
    }

    /**
     * 登录校验 | 获取数据数据权限
     *
     * @param roleList 角色信息集合
     * @param user     用户对象
     * @return 数据权限对象
     */
    @Override
    public DataScope getDataScope(List<SysRoleDto> roleList, SysUserDto user) {
        DataScope scope = new DataScope();
        // 1.判断是否为超管用户
        if (user.isAdmin()) {
            scope.setDataScope(AuthorityConstants.DataScope.ALL.getCode());
            return scope;
        }
        // 2.判断有无全部数据权限角色
        for (SysRoleDto role : roleList) {
            if (StrUtil.equals(role.getDataScope(), AuthorityConstants.DataScope.ALL.getCode())) {
                scope.setDataScope(AuthorityConstants.DataScope.ALL.getCode());
                return scope;
            }
        }
        // 3.组建权限集
        Set<Long> deptScope = new HashSet<>(), postScope = new HashSet<>(), userScope = new HashSet<>(), customRoleId = new HashSet<>();
        int isCustom = 0, isDept = 0, isDeptAndChild = 0, isPost = 0, isSelf = 0;
        for (SysRoleDto role : roleList) {
            switch (AuthorityConstants.DataScope.getByCode(role.getDataScope())) {
                case CUSTOM -> {
                    isCustom++;
                    customRoleId.add(role.getId());
                }
                case DEPT -> {
                    if (isDept++ == 0)
                        deptScope.addAll(user.getPosts().stream().map(post -> post.getDept().getId()).collect(Collectors.toSet()));
                }
                case DEPT_AND_CHILD -> {
                    if (isDeptAndChild++ == 0) {
                        Set<Long> deptIds = user.getPosts().stream().map(post -> post.getDept().getId()).collect(Collectors.toSet());
                        if (CollUtil.isNotEmpty(deptIds)) {
                            List<SysDeptDto> deptList = deptService.selectChildListByIds(deptIds);
                            deptScope.addAll(deptList.stream().map(SysDeptDto::getId).collect(Collectors.toSet()));
                        }
                    }
                }
                case POST -> {
                    if (isPost++ == 0)
                        postScope.addAll(user.getPosts().stream().map(SysPostDto::getId).collect(Collectors.toSet()));
                }
                case SELF -> {
                    if (isSelf++ == 0)
                        userScope.add(user.getId());
                }
                default -> {
                }
            }
        }

        if (isCustom > 0) {
            deptScope.addAll(organizeService.selectRoleDeptSetByRoleIds(customRoleId));
            postScope.addAll(organizeService.selectRolePostSetByRoleIds(customRoleId));
        }
        scope.setDeptScope(deptScope);
        List<SysPostDto> postList = postService.selectListByDeptIds(deptScope);
        postScope.addAll(postList.stream().map(SysPostDto::getId).collect(Collectors.toSet()));
        scope.setPostScope(postScope);
        userScope.addAll(organizeService.selectUserSetByPostIds(postScope));
        scope.setUserScope(userScope);
        if (isCustom > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.CUSTOM.getCode());
            return scope;
        } else if (isDeptAndChild > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.DEPT_AND_CHILD.getCode());
            return scope;
        } else if (isDept > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.DEPT.getCode());
            return scope;
        } else if (isPost > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.POST.getCode());
            return scope;
        } else if (isSelf > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.SELF.getCode());
            return scope;
        }
        scope.setDataScope(AuthorityConstants.DataScope.NONE.getCode());
        return scope;
    }

    /**
     * 登录校验 | 获取路由路径集合
     *
     * @param menuList 菜单信息对象集合
     * @return 路由路径集合
     */
    @Override
    public Map<String, String> getMenuRouteMap(List<SysMenuDto> menuList) {
        return buildRoutePath(menuList.stream().filter(menu -> (menu.isDir() || menu.isMenu() || menu.isDetails()))
                .collect(Collectors.toList()));
    }

    /**
     * 构建路由路径集合
     *
     * @param menus 菜单列表
     * @return 路径集合
     */
    private Map<String, String> buildRoutePath(List<SysMenuDto> menus) {
        Map<String, String> routeMap = new HashMap<>();
        if (CollUtil.isEmpty(menus))
            return new HashMap<>();
        List<SysMenuDto> menuTree = TreeUtil.buildTree(menus);
        SysMenuDto menu = new SysMenuDto();
        menu.setFullPath(StrUtil.EMPTY);
        menu.setChildren(menuTree);
        menuTree.forEach(sonChild -> {
            if (sonChild.isDetails())
                routeMap.put(sonChild.getName(), StrUtil.SLASH + sonChild.getPath());
        });
        recursionFn(menu, routeMap);
        return routeMap;
    }

    /**
     * 递归菜单树
     *
     * @param menu     菜单对象
     * @param routeMap 路径集合
     */
    private void recursionFn(SysMenuDto menu, Map<String, String> routeMap) {
        if (CollUtil.isNotEmpty(menu.getChildren())) {
            menu.getChildren().forEach(sonChild -> {
                sonChild.setFullPath(menu.getFullPath() + StrUtil.SLASH + sonChild.getPath());
                if (!sonChild.isDetails())
                    routeMap.put(sonChild.getName(), sonChild.getFullPath());
                if (CollUtil.isNotEmpty(sonChild.getChildren())) {
                    sonChild.getChildren().forEach(grandsonChild -> {
                        if (grandsonChild.isDetails()) {
                            String detailsSuffix = grandsonChild.getDetailsSuffix();
                            grandsonChild.setFullPath(StrUtil.isNotEmpty(detailsSuffix) ? menu.getFullPath() + StrUtil.SLASH + detailsSuffix : menu.getFullPath());
                            routeMap.put(grandsonChild.getName(), grandsonChild.getFullPath());
                        }
                    });
                    recursionFn(sonChild, routeMap);
                }
            });
        }
    }
}
