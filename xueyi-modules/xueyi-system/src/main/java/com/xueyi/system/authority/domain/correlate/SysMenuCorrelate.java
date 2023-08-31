package com.xueyi.system.authority.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.authority.domain.merge.SysAuthGroupMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.mapper.merge.SysAuthGroupMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.service.ISysModuleService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 权限模块 | 菜单 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysMenuCorrelate implements CorrelateService {

    EN_INFO_SELECT("企业查询|（企业信息）", new ArrayList<>() {{
        // 菜单 | 企业信息
        add(new Direct<>(SELECT, ISysEnterpriseService.class, SysMenuDto::getTenantId, SysEnterpriseDto::getId, SysMenuDto::getEnterpriseInfo));
    }}),
    INFO_LIST("默认列表|（模块）", new ArrayList<>() {{
        // 菜单 | 模块
        add(new Direct<>(SELECT, ISysModuleService.class, SysMenuDto::getModuleId, SysModuleDto::getId, SysMenuDto::getModule));
    }}),
    BASE_DEL("默认删除|（角色-菜单关联 | 企业权限组-菜单关联）", new ArrayList<>() {{
        // 菜单 | 角色-菜单关联
        add(new Indirect<>(DELETE, SysRoleMenuMergeMapper.class, SysRoleMenuMerge::getMenuId, SysMenuDto::getId));
        // 模块 | 企业权限组-菜单关联
        add(new Indirect<>(DELETE, SysAuthGroupMenuMergeMapper.class, SysAuthGroupMenuMerge::getMenuId, SysMenuDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
