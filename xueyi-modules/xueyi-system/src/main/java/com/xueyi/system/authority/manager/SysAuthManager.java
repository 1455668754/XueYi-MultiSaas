package com.xueyi.system.authority.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;
import com.xueyi.system.authority.domain.merge.SysTenantModuleMerge;
import com.xueyi.system.authority.domain.vo.SysAuthVo;
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

    /**
     * 获取公共模块|菜单权限树 | 租户端
     *
     * @return 权限对象集合
     */
    public List<SysAuthVo> selectCommonAuthScope() {
        List<SysModuleDto> modules = moduleManager.selectCommonList(null);
        List<SysMenuDto> menus = menuManager.selectCommonList(null);
        List<SysAuthVo> list = new ArrayList<>();
        list.addAll(modules.stream().map(SysAuthVo::new).collect(Collectors.toList()));
        list.addAll(menus.stream().map(SysAuthVo::new).collect(Collectors.toList()));
        return list;
    }

    /**
     * 新增租户权限
     *
     * @param authIds 权限Ids
     */
    public void addTenantAuthInner(Long[] authIds) {
        List<Long> menuIdList = Arrays.asList(authIds);
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
        List<Long> menuIdList = Arrays.asList(authIds);
        if (CollUtil.isNotEmpty(menuIdList)) {
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(menuIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                // 1.权限Ids中的模块Ids与菜单Ids分开
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                menuIdList.removeAll(moduleIdList);
                // 2.查询原始的租户与模块关联数据
                List<SysTenantModuleMerge> originalModuleList = tenantModuleMergeMapper.selectList(Wrappers.query());
                if (CollUtil.isNotEmpty(originalModuleList)) {
                    // 3.删除被移除的租户与模块的关联数据
                    List<Long> originalModuleIds = originalModuleList.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
                    List<Long> delModuleIds = new ArrayList<>(originalModuleIds);
                    delModuleIds.removeAll(menuIdList);
                    if (CollUtil.isNotEmpty(delModuleIds)) {
                        tenantModuleMergeMapper.deleteBatchIds(delModuleIds);
                    }
                    menuIdList.removeAll(originalModuleIds);
                }
                // 4.存储新增的租户与模块的关联数据
                List<SysTenantModuleMerge> tenantModuleMerges = moduleIdList.stream().map(SysTenantModuleMerge::new).collect(Collectors.toList());
                tenantModuleMergeMapper.insertBatch(tenantModuleMerges);
            }
            // 5.查询原始的租户与菜单关联数据
            List<SysTenantMenuMerge> originalMenuList = tenantMenuMergeMapper.selectList(Wrappers.query());
            if (CollUtil.isNotEmpty(originalMenuList)) {
                // 6.删除被移除的租户与菜单的关联数据
                List<Long> originalMenuIds = originalMenuList.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
                List<Long> delMenuIds = new ArrayList<>(originalMenuIds);
                delMenuIds.removeAll(menuIdList);
                if (CollUtil.isNotEmpty(delMenuIds)) {
                    tenantMenuMergeMapper.deleteBatchIds(delMenuIds);
                }
                menuIdList.removeAll(originalMenuIds);
            }
            // 7.存储新增的租户与菜单的关联数据
            List<SysTenantMenuMerge> tenantMenuMerges = menuIdList.stream().map(SysTenantMenuMerge::new).collect(Collectors.toList());
            tenantMenuMergeMapper.insertBatch(tenantMenuMerges);
        }
    }
}
