package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.OrganizeConstants;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleOrganizeMerge;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleOrganizeMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysOrganizeManager {

    @Autowired
    private SysDeptManager deptManager;

    @Autowired
    private SysPostManager postManager;

    @Autowired
    private SysRoleOrganizeMergeMapper roleOrganizeMergeMapper;

    @Autowired
    private SysOrganizeRoleMergeMapper organizeRoleMergeMapper;

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    public List<SysOrganizeTree> selectOrganizeScope() {
        List<SysDeptDto> deptList = deptManager.selectList(null);
        List<SysPostDto> postList = postManager.selectList(null);
        return new ArrayList<>(CollUtil.addAll(
                postList.stream().map(SysOrganizeTree::new).collect(Collectors.toList()),
                deptList.stream().map(SysOrganizeTree::new).collect(Collectors.toList())));
    }

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    public Long[] selectRoleOrganizeMerge(Long roleId) {
        List<SysRoleOrganizeMerge> roleOrganizeMerges = roleOrganizeMergeMapper.selectList(
                Wrappers.<SysRoleOrganizeMerge>query().lambda()
                        .eq(SysRoleOrganizeMerge::getRoleId, roleId));
        return roleOrganizeMerges.stream().map(SysRoleOrganizeMerge::getOrganizeId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 根据部门Id获取关联的角色Ids
     *
     * @param deptId 部门Id
     * @return 角色Ids
     */
    public Long[] selectDeptRoleMerge(Long deptId) {
        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                Wrappers.<SysOrganizeRoleMerge>query().lambda()
                        .eq(SysOrganizeRoleMerge::getDeptId, deptId));
        return organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 根据岗位Id获取关联的角色Ids
     *
     * @param postId 岗位Id
     * @return 角色Ids
     */
    public Long[] selectPostRoleMerge(Long postId) {
        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                Wrappers.<SysOrganizeRoleMerge>query().lambda()
                        .eq(SysOrganizeRoleMerge::getPostId, postId));
        return organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 根据用户Id获取关联的角色Ids
     *
     * @param userId 用户Id
     * @return 角色Ids
     */
    public Long[] selectUserRoleMerge(Long userId) {
        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                Wrappers.<SysOrganizeRoleMerge>query().lambda()
                        .eq(SysOrganizeRoleMerge::getUserId, userId));
        return organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    public void addRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        List<SysRoleOrganizeMerge> roleMenuMerges = Arrays.stream(organizeIds).map(organizeId -> new SysRoleOrganizeMerge(roleId, organizeId)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(roleMenuMerges))
            roleOrganizeMergeMapper.insertBatch(roleMenuMerges);
    }


    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    public void editRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        // 校验authIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(organizeIds)) {
            List<Long> organizeIdList = new ArrayList<>(Arrays.asList(organizeIds));
            List<SysRoleOrganizeMerge> originalOrganizeList = roleOrganizeMergeMapper.selectList(
                    Wrappers.<SysRoleOrganizeMerge>query().lambda()
                            .eq(SysRoleOrganizeMerge::getRoleId, roleId));
            if (CollUtil.isNotEmpty(originalOrganizeList)) {
                List<Long> originalOrganizeIds = originalOrganizeList.stream().map(SysRoleOrganizeMerge::getOrganizeId).collect(Collectors.toList());
                List<Long> delOrganizeIds = new ArrayList<>(originalOrganizeIds);
                delOrganizeIds.removeAll(organizeIdList);
                if (CollUtil.isNotEmpty(delOrganizeIds)) {
                    roleOrganizeMergeMapper.delete(
                            Wrappers.<SysRoleOrganizeMerge>query().lambda()
                                    .eq(SysRoleOrganizeMerge::getRoleId, roleId)
                                    .in(SysRoleOrganizeMerge::getOrganizeId, delOrganizeIds));
                }
                organizeIdList.removeAll(originalOrganizeIds);
            }
            if (CollUtil.isNotEmpty(organizeIdList)) {
                List<SysRoleOrganizeMerge> roleOrganizeMerges = organizeIdList.stream().map(organizeId -> new SysRoleOrganizeMerge(roleId, organizeId)).collect(Collectors.toList());
                roleOrganizeMergeMapper.insertBatch(roleOrganizeMerges);
            }
        } else {
            roleOrganizeMergeMapper.delete(
                    Wrappers.<SysRoleOrganizeMerge>query().lambda()
                            .in(SysRoleOrganizeMerge::getRoleId, roleId));
        }
    }

    /**
     * 修改部门的角色关联数据
     *
     * @param deptId  部门Id
     * @param roleIds 角色Ids
     */
    public void editDeptRoleMerge(Long deptId, Long[] roleIds) {
        // 校验roleIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(roleIds)) {
            List<Long> roleIdList = new ArrayList<>(Arrays.asList(roleIds));
            List<SysOrganizeRoleMerge> originalRoleList = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getDeptId, deptId));
            if (CollUtil.isNotEmpty(originalRoleList)) {
                List<Long> originalRoleIds = originalRoleList.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList());
                List<Long> delRoleIds = new ArrayList<>(originalRoleIds);
                delRoleIds.removeAll(roleIdList);
                if (CollUtil.isNotEmpty(delRoleIds)) {
                    organizeRoleMergeMapper.delete(
                            Wrappers.<SysOrganizeRoleMerge>query().lambda()
                                    .eq(SysOrganizeRoleMerge::getDeptId, deptId)
                                    .in(SysOrganizeRoleMerge::getRoleId, delRoleIds));
                }
                roleIdList.removeAll(originalRoleIds);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SysOrganizeRoleMerge> organizeRoleMerges = roleIdList.stream().map(roleId -> new SysOrganizeRoleMerge(deptId, roleId, OrganizeConstants.OrganizeType.DEPT)).collect(Collectors.toList());
                organizeRoleMergeMapper.insertBatch(organizeRoleMerges);
            }
        } else {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .in(SysOrganizeRoleMerge::getDeptId, deptId));
        }
    }

    /**
     * 修改岗位的角色关联数据
     *
     * @param postId  岗位Id
     * @param roleIds 角色Ids
     */
    public void editPostIdRoleMerge(Long postId, Long[] roleIds) {
        // 校验roleIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(roleIds)) {
            List<Long> roleIdList = new ArrayList<>(Arrays.asList(roleIds));
            List<SysOrganizeRoleMerge> originalRoleList = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getPostId, postId));
            if (CollUtil.isNotEmpty(originalRoleList)) {
                List<Long> originalRoleIds = originalRoleList.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList());
                List<Long> delRoleIds = new ArrayList<>(originalRoleIds);
                delRoleIds.removeAll(roleIdList);
                if (CollUtil.isNotEmpty(delRoleIds)) {
                    organizeRoleMergeMapper.delete(
                            Wrappers.<SysOrganizeRoleMerge>query().lambda()
                                    .eq(SysOrganizeRoleMerge::getPostId, postId)
                                    .in(SysOrganizeRoleMerge::getRoleId, delRoleIds));
                }
                roleIdList.removeAll(originalRoleIds);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SysOrganizeRoleMerge> organizeRoleMerges = roleIdList.stream().map(roleId -> new SysOrganizeRoleMerge(postId, roleId, OrganizeConstants.OrganizeType.POST)).collect(Collectors.toList());
                organizeRoleMergeMapper.insertBatch(organizeRoleMerges);
            }
        } else {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .in(SysOrganizeRoleMerge::getPostId, postId));
        }
    }

    /**
     * 修改用户的角色关联数据
     *
     * @param userId  用户Id
     * @param roleIds 角色Ids
     */
    public void editUserRoleMerge(Long userId, Long[] roleIds) {
        // 校验roleIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(roleIds)) {
            List<Long> roleIdList = new ArrayList<>(Arrays.asList(roleIds));
            List<SysOrganizeRoleMerge> originalRoleList = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getUserId, userId));
            if (CollUtil.isNotEmpty(originalRoleList)) {
                List<Long> originalRoleIds = originalRoleList.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList());
                List<Long> delRoleIds = new ArrayList<>(originalRoleIds);
                delRoleIds.removeAll(roleIdList);
                if (CollUtil.isNotEmpty(delRoleIds)) {
                    organizeRoleMergeMapper.delete(
                            Wrappers.<SysOrganizeRoleMerge>query().lambda()
                                    .eq(SysOrganizeRoleMerge::getUserId, userId)
                                    .in(SysOrganizeRoleMerge::getRoleId, delRoleIds));
                }
                roleIdList.removeAll(originalRoleIds);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SysOrganizeRoleMerge> organizeRoleMerges = roleIdList.stream().map(roleId -> new SysOrganizeRoleMerge(userId, roleId, OrganizeConstants.OrganizeType.USER)).collect(Collectors.toList());
                organizeRoleMergeMapper.insertBatch(organizeRoleMerges);
            }
        } else {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .in(SysOrganizeRoleMerge::getUserId, userId));
        }
    }
}
