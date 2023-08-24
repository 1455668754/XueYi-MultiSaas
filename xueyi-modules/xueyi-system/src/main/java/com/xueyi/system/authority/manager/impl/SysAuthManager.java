package com.xueyi.system.authority.manager.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.manager.ISysAuthManager;
import com.xueyi.system.authority.manager.ISysMenuManager;
import com.xueyi.system.authority.manager.ISysModuleManager;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 权限管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysAuthManager implements ISysAuthManager {

    @Autowired
    private ISysModuleManager moduleManager;

    @Autowired
    private ISysMenuManager menuManager;

    /**
     * 获取公共模块 | 菜单权限树
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectCommonAuthScope() {
        List<SysModuleDto> modules = moduleManager.selectCommonList();
        List<SysMenuDto> menus = menuManager.selectCommonList();
        return new ArrayList<>(CollUtil.addAll(
                modules.stream().map(SysAuthTree::new).collect(Collectors.toList()),
                menus.stream().map(SysAuthTree::new).collect(Collectors.toList())));
    }

    /**
     * 获取企业模块 | 菜单权限树 | 用户范围内
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectEnterpriseAuthScope() {
        LoginUser loginUser = SecurityUserUtils.getLoginUser();
        DataScope dataScope = loginUser.getDataScope();
        List<SysModuleDto> modules = moduleManager.selectEnterpriseList(dataScope.getAuthGroupIds(), dataScope.getRoleIds(), loginUser.getIsLessor(), loginUser.getUserType());
        List<SysMenuDto> menus = menuManager.selectEnterpriseList(dataScope.getAuthGroupIds(), dataScope.getRoleIds(), loginUser.getIsLessor(), loginUser.getUserType());
        return new ArrayList<>(CollUtil.addAll(
                modules.stream().map(SysAuthTree::new).collect(Collectors.toList()),
                menus.stream().map(SysAuthTree::new).collect(Collectors.toList())));
    }
}
