package com.xueyi.system.organize.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRolePostMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRolePostMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import com.xueyi.system.organize.service.ISysDeptService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 组织模块 | 岗位 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysPostCorrelate implements CorrelateService {

    BASE_LIST("默认列表|关联（岗位）", new ArrayList<>() {{
        add(new Direct<>(SELECT, ISysDeptService.class, SysPostDto::getDeptId, SysDeptDto::getId, SysPostDto::getDept));
    }}),
    BASE_DEL("默认删除|关联（岗位）", new ArrayList<>() {{
        add(new Indirect<>(DELETE, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge::getPostId, SysPostDto::getId));
        add(new Indirect<>(DELETE, SysRolePostMergeMapper.class, SysRolePostMerge::getPostId, SysPostDto::getId));
        add(new Indirect<>(DELETE, SysUserPostMergeMapper.class, SysUserPostMerge::getPostId, SysPostDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}