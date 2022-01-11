package com.xueyi.system.organize.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.web.entity.controller.SubTreeController;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController extends SubTreeController<SysDeptDto, ISysDeptService, SysPostDto, ISysPostService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "部门";
    }

    /** 定义子数据名称 */
    protected String getSubName() {
        return "岗位";
    }
//    /**
//     * 修改部门-角色关系
//     */
//    @RequiresPermissions("system:role:set")
//    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/changeDeptRole")
//    public AjaxResult editDeptRole(@Validated @RequestBody SysDept dept) {
//        return toAjax(deptService.updateDeptRole(dept));
//    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysDeptDto dept) {
        if (baseService.checkDeptCodeUnique(dept.getId(), dept.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，部门编码已存在", operate.getInfo(), getNodeName(), dept.getName()));
        else if (baseService.checkNameUnique(dept.getId(), dept.getParentId(), dept.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，部门名称已存在", operate.getInfo(), getNodeName(), dept.getName()));
    }
}
