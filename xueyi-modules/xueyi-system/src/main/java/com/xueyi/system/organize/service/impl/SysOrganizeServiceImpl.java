package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.manager.SysOrganizeManager;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 组织管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysOrganizeServiceImpl implements ISysOrganizeService {

    @Autowired
    private SysOrganizeManager organizeManager;

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeScope() {
        return organizeManager.selectOrganizeScope();
    }

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    @Override
    public Long[] selectRoleOrganize(Long roleId){
        return organizeManager.selectRoleOrganize(roleId);
    }

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    @Override
    public void addRoleOrganize(Long roleId, Long[] organizeIds) {
        organizeManager.addRoleOrganize(roleId, organizeIds);
    }


    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    @Override
    public void editRoleOrganize(Long roleId, Long[] organizeIds) {
        organizeManager.editRoleOrganize(roleId, organizeIds);
    }
}
