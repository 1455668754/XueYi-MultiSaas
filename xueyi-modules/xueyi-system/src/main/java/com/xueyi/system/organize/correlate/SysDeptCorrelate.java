package com.xueyi.system.organize.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import com.xueyi.system.organize.service.ISysPostService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 组织模块 | 部门 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysDeptCorrelate implements CorrelateService {

    BASE_LIST("默认列表|关联（部门）", new ArrayList<>() {{
        add(new Direct<>(SELECT, ISysPostService.class, SysDeptDto::getId, SysPostDto::getDeptId, SysDeptDto::getSubList));
    }}),
    BASE_DEL("默认删除|关联（部门）", new ArrayList<>() {{
        add(new Direct<>(DELETE, ISysPostService.class, SysPostDto::getDeptId, SysDeptDto::getId));
        add(new Indirect<>(DELETE, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge::getDeptId, SysDeptDto::getId));
        add(new Indirect<>(DELETE, SysRoleDeptMergeMapper.class, SysRoleDeptMerge::getDeptId, SysDeptDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}