package com.xueyi.system.authority.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.authority.domain.merge.SysAuthGroupModuleMerge;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.mapper.merge.SysAuthGroupModuleMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 权限模块 | 模块 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysModuleCorrelate implements CorrelateService {

    EN_INFO_SELECT("企业查询|（企业信息）", new ArrayList<>() {{
        // 模块 | 企业信息
        add(new Direct<>(SELECT, ISysEnterpriseService.class, SysModuleDto::getTenantId, SysEnterpriseDto::getId, SysModuleDto::getEnterpriseInfo));
    }}),
    BASE_DEL("默认删除|（菜单 | 菜单|角色-模块关联 | 企业权限组-模块关联）", new ArrayList<>() {{
        // 模块 | 菜单
        add(new Direct<>(DELETE, ISysMenuService.class, SysModuleDto::getId, SysMenuDto::getModuleId));
        // 模块 | 角色-模块关联
        add(new Indirect<>(DELETE, SysRoleModuleMergeMapper.class, SysRoleModuleMerge::getModuleId, SysModuleDto::getId));
        // 模块 | 企业权限组-模块关联
        add(new Indirect<>(DELETE, SysAuthGroupModuleMergeMapper.class, SysAuthGroupModuleMerge::getModuleId, SysModuleDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
