package com.xueyi.system.authority.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.merge.SysAuthGroupMenuMerge;
import com.xueyi.system.authority.domain.merge.SysAuthGroupModuleMerge;
import com.xueyi.system.authority.domain.merge.SysTenantAuthGroupMerge;
import com.xueyi.system.authority.mapper.merge.SysAuthGroupMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysAuthGroupModuleMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantAuthGroupMergeMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.ADD;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.EDIT;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 权限模块 | 企业权限组 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysAuthGroupCorrelate implements CorrelateService {

    INFO_LIST("默认列表|（企业权限组-菜单关联 | 企业权限组-模块关联）", new ArrayList<>() {{
        // 企业权限组 | 企业权限组-菜单关联
        add(new Indirect<>(SELECT, SysAuthGroupMenuMergeMapper.class, SysAuthGroupMenuMerge::getAuthGroupId, SysAuthGroupMenuMerge::getMenuId, SysAuthGroupDto::getId, SysAuthGroupDto::getMenuIds));
        // 企业权限组 | 企业权限组-模块关联
        add(new Indirect<>(SELECT, SysAuthGroupModuleMergeMapper.class, SysAuthGroupModuleMerge::getAuthGroupId, SysAuthGroupModuleMerge::getModuleId, SysAuthGroupDto::getId, SysAuthGroupDto::getModuleIds));
    }}),
    BASE_ADD("默认新增|（企业权限组-菜单关联 | 企业权限组-模块关联）", new ArrayList<>() {{
        // 企业权限组 | 企业权限组-菜单关联
        add(new Indirect<>(ADD, SysAuthGroupMenuMergeMapper.class, SysAuthGroupMenuMerge::getAuthGroupId, SysAuthGroupMenuMerge::getMenuId, SysAuthGroupDto::getId, SysAuthGroupDto::getMenuIds));
        // 企业权限组 | 企业权限组-模块关联
        add(new Indirect<>(ADD, SysAuthGroupModuleMergeMapper.class, SysAuthGroupModuleMerge::getAuthGroupId, SysAuthGroupModuleMerge::getModuleId, SysAuthGroupDto::getId, SysAuthGroupDto::getModuleIds));
    }}),
    BASE_EDIT("默认修改|（企业权限组-菜单关联 | 企业权限组-模块关联）", new ArrayList<>() {{
        // 企业权限组 | 企业权限组-菜单关联
        add(new Indirect<>(SELECT, SysAuthGroupMenuMergeMapper.class, SysAuthGroupMenuMerge::getAuthGroupId, SysAuthGroupMenuMerge::getMenuId, SysAuthGroupDto::getId, SysAuthGroupDto::getMenuIds));
        // 企业权限组 | 企业权限组-模块关联
        add(new Indirect<>(SELECT, SysAuthGroupModuleMergeMapper.class, SysAuthGroupModuleMerge::getAuthGroupId, SysAuthGroupModuleMerge::getModuleId, SysAuthGroupDto::getId, SysAuthGroupDto::getModuleIds));
        // 企业权限组 | 企业权限组-菜单关联
        add(new Indirect<>(EDIT, SysAuthGroupMenuMergeMapper.class, SysAuthGroupMenuMerge::getAuthGroupId, SysAuthGroupMenuMerge::getMenuId, SysAuthGroupDto::getId, SysAuthGroupDto::getMenuIds));
        // 企业权限组 | 企业权限组-模块关联
        add(new Indirect<>(EDIT, SysAuthGroupModuleMergeMapper.class, SysAuthGroupModuleMerge::getAuthGroupId, SysAuthGroupModuleMerge::getModuleId, SysAuthGroupDto::getId, SysAuthGroupDto::getModuleIds));
    }}),
    BASE_DEL("默认删除|（企业权限组-菜单关联 | 企业权限组-模块关联 | 企业-企业权限组关联）", new ArrayList<>() {{
        // 企业权限组 | 企业权限组-菜单关联
        add(new Indirect<>(DELETE, SysAuthGroupMenuMergeMapper.class, SysAuthGroupMenuMerge::getAuthGroupId, SysAuthGroupDto::getId));
        // 企业权限组 | 企业权限组-模块关联
        add(new Indirect<>(DELETE, SysAuthGroupModuleMergeMapper.class, SysAuthGroupModuleMerge::getAuthGroupId, SysAuthGroupDto::getId));
        // 企业权限组 | 企业-企业权限组关联
        add(new Indirect<>(DELETE, SysTenantAuthGroupMergeMapper.class, SysTenantAuthGroupMerge::getAuthGroupId, SysAuthGroupDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}