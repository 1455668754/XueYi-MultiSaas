package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.domain.merge.SysRolePostMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.manager.ISysDeptManager;
import com.xueyi.system.organize.manager.ISysOrganizeManager;
import com.xueyi.system.organize.manager.ISysPostManager;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRolePostMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统服务 | 组织模块 | 组织管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysOrganizeManagerImpl implements ISysOrganizeManager {

    @Autowired
    private ISysDeptManager deptManager;

    @Autowired
    private ISysPostManager postManager;

    @Autowired
    private SysUserPostMergeMapper userPostMergeMapper;

    @Autowired
    private SysRoleDeptMergeMapper roleDeptMergeMapper;

    @Autowired
    private SysRolePostMergeMapper rolePostMergeMapper;

    /**
     * 登录校验 | 根据角色Ids获取关联部门
     *
     * @param roleIds 角色Ids
     * @return 部门Id集合
     */
    @Override
    public Set<Long> selectRoleDeptSetByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds))
            return new HashSet<>();
        List<SysRoleDeptMerge> roleDeptMerges = roleDeptMergeMapper.selectList(
                Wrappers.<SysRoleDeptMerge>lambdaQuery()
                        .in(SysRoleDeptMerge::getRoleId, roleIds));
        return roleDeptMerges.stream().map(SysRoleDeptMerge::getDeptId).collect(Collectors.toSet());
    }

    /**
     * 登录校验 | 根据角色Ids获取关联岗位
     *
     * @param roleIds 角色Ids
     * @return 岗位Id集合
     */
    @Override
    public Set<Long> selectRolePostSetByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds))
            return new HashSet<>();
        List<SysRolePostMerge> rolePostMerges = rolePostMergeMapper.selectList(
                Wrappers.<SysRolePostMerge>lambdaQuery()
                        .in(SysRolePostMerge::getRoleId, roleIds));
        return rolePostMerges.stream().map(SysRolePostMerge::getPostId).collect(Collectors.toSet());
    }

    /**
     * 登录校验 | 根据岗位Ids获取归属用户Ids集合
     *
     * @param postIds 岗位Ids
     * @return 用户Ids集合
     */
    @Override
    public Set<Long> selectUserSetByPostIds(Collection<Long> postIds) {
        if (CollUtil.isEmpty(postIds))
            return new HashSet<>();
        List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                Wrappers.<SysUserPostMerge>lambdaQuery()
                        .in(SysUserPostMerge::getPostId, postIds));
        return userPostMerges.stream().map(SysUserPostMerge::getUserId).collect(Collectors.toSet());
    }

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeScope() {
        List<SysDeptDto> deptList = deptManager.selectList(null);
        List<SysPostDto> postList = postManager.selectList(null);
        return new ArrayList<>(CollUtil.addAll(
                postList.stream().map(SysOrganizeTree::new).collect(Collectors.toList()),
                deptList.stream().map(SysOrganizeTree::new).collect(Collectors.toList()))
        );
    }
}
