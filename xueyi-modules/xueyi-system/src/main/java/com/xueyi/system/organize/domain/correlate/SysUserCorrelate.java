package com.xueyi.system.organize.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysPostService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.*;

/**
 * 系统服务 | 组织模块 | 用户 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysUserCorrelate implements CorrelateService {

    ROLE_SEL("角色组查询|关联（组织-角色）", new ArrayList<>() {{
        // 用户 | 组织-角色
        add(new Indirect<>(SELECT, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge::getUserId, SysOrganizeRoleMerge::getRoleId, SysUserDto::getId, SysUserDto::getRoleIds));
    }}),
    ROLE_EDIT("角色组查询|关联（组织-角色）", new ArrayList<>() {{
        // 用户 | 组织-角色
        add(new Indirect<>(EDIT, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge::getUserId, SysOrganizeRoleMerge::getRoleId, SysUserDto::getId, SysUserDto::getRoleIds));
    }}),
    INFO_LIST("默认列表|（岗位）", new ArrayList<>() {{
        // 用户 | 岗位
        add(new Indirect<>(SELECT, ISysPostService.class, SysUserPostMergeMapper.class, SysUserPostMerge::getUserId, SysUserPostMerge::getPostId, SysUserDto::getId, SysPostDto::getId, SysUserDto::getPostIds, SysUserDto::getPosts, new ArrayList<>() {{
            // 岗位 | 部门
            add(new Direct<>(SELECT, ISysDeptService.class, SysPostDto::getDeptId, SysDeptDto::getId, SysPostDto::getDept));
        }}));
    }}),
    BASE_ADD("默认新增|（用户-岗位）", new ArrayList<>() {{
        // 用户 | 岗位
        add(new Indirect<>(ADD, ISysPostService.class, SysUserPostMergeMapper.class, SysUserPostMerge::getUserId, SysUserPostMerge::getPostId, SysUserDto::getId, SysPostDto::getId, SysUserDto::getPostIds, SysUserDto::getPosts));
    }}),
    BASE_EDIT("默认修改|（用户-岗位）", new ArrayList<>() {{
        // 用户 | 岗位
        add(new Indirect<>(EDIT, ISysPostService.class, SysUserPostMergeMapper.class, SysUserPostMerge::getUserId, SysUserPostMerge::getPostId, SysUserDto::getId, SysPostDto::getId, SysUserDto::getPostIds, SysUserDto::getPosts));
    }}),
    BASE_DEL("默认删除|（用户-岗位|组织-角色）", new ArrayList<>() {{
        // 用户 | 组织-角色
        add(new Indirect<>(DELETE, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge::getUserId, SysUserDto::getId));
        // 用户 | 用户-岗位
        add(new Indirect<>(DELETE, SysUserPostMergeMapper.class, SysUserPostMerge::getPostId, SysUserDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
