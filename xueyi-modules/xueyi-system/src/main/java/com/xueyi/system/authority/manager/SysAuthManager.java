package com.xueyi.system.authority.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;
import com.xueyi.system.authority.domain.merge.SysTenantModuleMerge;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantModuleMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysAuthManager {

    @Autowired
    private SysModuleManager moduleManager;

    @Autowired
    private SysMenuManager menuManager;

    @Autowired
    private SysTenantModuleMergeMapper tenantModuleMergeMapper;

    @Autowired
    private SysTenantMenuMergeMapper tenantMenuMergeMapper;

    @Autowired
    private SysRoleModuleMergeMapper roleModuleMergeMapper;

    @Autowired
    private SysRoleMenuMergeMapper roleMenuMergeMapper;

    /**
     * 获取公共模块|菜单权限树
     *
     * @return 权限对象集合
     */
    public List<SysAuthTree> selectCommonAuthScope() {
        List<SysModuleDto> modules = moduleManager.selectCommonList();
        List<SysMenuDto> menus = menuManager.selectCommonList();
        List<SysAuthTree> list = new ArrayList<>();
        list.addAll(modules.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        list.addAll(menus.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        return list;
    }

    /**
     * 获取企业模块|菜单权限树
     *
     * @return 权限对象集合
     */
    public List<SysAuthTree> selectEnterpriseAuthScope() {
//        return authManager.selectEnterpriseAuthScope();
        return null;
    }

    /**
     * 获取租户权限
     *
     * @return 权限集合
     */
    public Long[] selectTenantAuthInner() {
        List<SysAuthTree> treeList = new ArrayList<>();
        List<SysTenantModuleMerge> tenantModuleMerges = tenantModuleMergeMapper.selectList(Wrappers.query());
        if (CollUtil.isNotEmpty(tenantModuleMerges)) {
            List<Long> moduleIds = tenantModuleMerges.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(moduleIds);
            treeList.addAll(moduleList.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        }
        List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(Wrappers.query());
        if (CollUtil.isNotEmpty(tenantMenuMerges)) {
            List<Long> menuIds = tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
            System.out.println(menuIds);
            List<SysMenuDto> menuList = menuManager.selectListByIds(menuIds);
            System.out.println(menuList);
            treeList.addAll(menuList.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        }
        System.out.println(treeList);
        System.out.println("treeList");
        System.out.println(TreeUtils.buildTree(treeList));
        List<SysAuthTree> leafNodes = TreeUtils.getLeafNodes(TreeUtils.buildTree(treeList));
        return leafNodes.stream().map(SysAuthTree::getId).toArray(Long[]::new);
    }

    /**
     * 新增租户权限
     *
     * @param authIds 权限Ids
     */
    public void addTenantAuthInner(Long[] authIds) {
        List<Long> menuIdList = new ArrayList<>(Arrays.asList(authIds));
        if (CollUtil.isNotEmpty(menuIdList)) {
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(menuIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                // 1.权限Ids中的模块Ids与菜单Ids分开
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                menuIdList.removeAll(moduleIdList);
                // 2.存储租户与模块的关联数据
                List<SysTenantModuleMerge> tenantModuleMerges = moduleIdList.stream().map(SysTenantModuleMerge::new).collect(Collectors.toList());
                tenantModuleMergeMapper.insertBatch(tenantModuleMerges);
            }
            // 3.存储租户与菜单的关联数据
            List<SysTenantMenuMerge> tenantMenuMerges = menuIdList.stream().map(SysTenantMenuMerge::new).collect(Collectors.toList());
            tenantMenuMergeMapper.insertBatch(tenantMenuMerges);
        }
    }

    /**
     * 修改租户权限
     *
     * @param authIds 权限Ids
     */
    public void editTenantAuthInner(Long[] authIds) {
        List<Long> menuIdList = new ArrayList<>(Arrays.asList(authIds));
        // 1.校验authIds是否为空? 删除不存在的,增加新增的 : 删除所有
        if (CollUtil.isNotEmpty(menuIdList)) {
            // 2.查询authIds中的模块Id，分离menuIds与moduleIds
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(menuIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                menuIdList.removeAll(moduleIdList);
                // 3.查询原始的租户与模块关联数据,新增/删除差异关联数据
                List<SysTenantModuleMerge> originalModuleList = tenantModuleMergeMapper.selectList(Wrappers.query());
                if (CollUtil.isNotEmpty(originalModuleList)) {
                    List<Long> originalModuleIds = originalModuleList.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
                    List<Long> delModuleIds = new ArrayList<>(originalModuleIds);
                    delModuleIds.removeAll(moduleIdList);
                    if (CollUtil.isNotEmpty(delModuleIds)) {
                        tenantModuleMergeMapper.delete(
                                Wrappers.<SysTenantModuleMerge>query().lambda()
                                        .in(SysTenantModuleMerge::getModuleId, delModuleIds));
                        roleModuleMergeMapper.delete(
                                Wrappers.<SysRoleModuleMerge>query().lambda()
                                        .in(SysRoleModuleMerge::getModuleId, delModuleIds));
                    }
                    moduleIdList.removeAll(originalModuleIds);
                }
                if (CollUtil.isNotEmpty(moduleIdList)) {
                    List<SysTenantModuleMerge> tenantModuleMerges = moduleIdList.stream().map(SysTenantModuleMerge::new).collect(Collectors.toList());
                    tenantModuleMergeMapper.insertBatch(tenantModuleMerges);
                }
            }
            // // 4.查询原始的租户与菜单关联数据,新增/删除差异关联数据
            List<SysTenantMenuMerge> originalMenuList = tenantMenuMergeMapper.selectList(Wrappers.query());
            if (CollUtil.isNotEmpty(originalMenuList)) {
                List<Long> originalMenuIds = originalMenuList.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
                List<Long> delMenuIds = new ArrayList<>(originalMenuIds);
                delMenuIds.removeAll(menuIdList);
                if (CollUtil.isNotEmpty(delMenuIds)) {
                    tenantMenuMergeMapper.delete(
                            Wrappers.<SysTenantMenuMerge>query().lambda()
                                    .in(SysTenantMenuMerge::getMenuId, delMenuIds));
                    roleMenuMergeMapper.delete(
                            Wrappers.<SysRoleMenuMerge>query().lambda()
                                    .in(SysRoleMenuMerge::getMenuId, delMenuIds));
                }
                menuIdList.removeAll(originalMenuIds);
            }
            if (CollUtil.isNotEmpty(menuIdList)) {
                List<SysTenantMenuMerge> tenantMenuMerges = menuIdList.stream().map(SysTenantMenuMerge::new).collect(Collectors.toList());
                tenantMenuMergeMapper.insertBatch(tenantMenuMerges);
            }
        } else {
            List<SysTenantModuleMerge> originalModuleList = tenantModuleMergeMapper.selectList(Wrappers.query());
            if (CollUtil.isNotEmpty(originalModuleList)) {
                List<Long> originalModuleIds = originalModuleList.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(originalModuleIds)) {
                    tenantModuleMergeMapper.delete(
                            Wrappers.<SysTenantModuleMerge>query().lambda()
                                    .in(SysTenantModuleMerge::getModuleId, originalModuleIds));
                    roleModuleMergeMapper.delete(
                            Wrappers.<SysRoleModuleMerge>query().lambda()
                                    .in(SysRoleModuleMerge::getModuleId, originalModuleIds));
                }
            }
            List<SysTenantMenuMerge> originalMenuList = tenantMenuMergeMapper.selectList(Wrappers.query());
            List<Long> originalMenuIds = originalMenuList.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(originalMenuIds)) {
                tenantMenuMergeMapper.delete(
                        Wrappers.<SysTenantMenuMerge>query().lambda()
                                .in(SysTenantMenuMerge::getMenuId, originalMenuIds));
                roleMenuMergeMapper.delete(
                        Wrappers.<SysRoleMenuMerge>query().lambda()
                                .in(SysRoleMenuMerge::getMenuId, originalMenuIds));
            }
        }
    }
}
