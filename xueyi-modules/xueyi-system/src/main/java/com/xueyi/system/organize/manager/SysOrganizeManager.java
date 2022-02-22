package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.merge.SysRoleOrganizeMerge;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
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
    public Long[] selectRoleOrganize(Long roleId){
        List<SysRoleOrganizeMerge> roleOrganizeMerges = roleOrganizeMergeMapper.selectList(
                Wrappers.<SysRoleOrganizeMerge>query().lambda()
                        .eq(SysRoleOrganizeMerge::getRoleId,roleId));
        return roleOrganizeMerges.stream().map(SysRoleOrganizeMerge::getOrganizeId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    public void addRoleOrganize(Long roleId, Long[] organizeIds) {
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
    public void editRoleOrganize(Long roleId, Long[] organizeIds) {
        List<Long> organizeIdList = new ArrayList<>(Arrays.asList(organizeIds));
        // 校验authIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (CollUtil.isNotEmpty(organizeIdList)) {
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
}
