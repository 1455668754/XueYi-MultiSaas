package com.xueyi.system.authority.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.domain.merge.SysRolePostMerge;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRolePostMergeMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;

/**
 * 角色 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysRoleCorrelate implements CorrelateService {

    BASE_DEL("默认删除|关联（角色）", new ArrayList<>() {{
        add(new Indirect<>(DELETE, SysRoleModuleMergeMapper.class, SysRoleModuleMerge::getRoleId, SysRoleDto::getId));
        add(new Indirect<>(DELETE, SysRoleMenuMergeMapper.class, SysRoleMenuMerge::getRoleId, SysRoleDto::getId));
        add(new Indirect<>(DELETE, SysRoleDeptMergeMapper.class, SysRoleDeptMerge::getRoleId, SysRoleDto::getId));
        add(new Indirect<>(DELETE, SysRolePostMergeMapper.class, SysRolePostMerge::getRoleId, SysRoleDto::getId));
        add(new Indirect<>(DELETE, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge::getRoleId, SysRoleDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}