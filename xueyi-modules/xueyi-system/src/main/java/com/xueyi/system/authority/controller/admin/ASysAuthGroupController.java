package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.system.authority.controller.base.BSysAuthGroupController;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 系统服务 | 权限模块 | 企业权限组管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/authGroup")
public class ASysAuthGroupController extends BSysAuthGroupController {

    /**
     * 查询企业权限组列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_AUTH_GROUP_LIST)")
    public AjaxResult list(SysAuthGroupQuery authGroup) {
        return super.list(authGroup);
    }

    /**
     * 查询企业权限组详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_AUTH_GROUP_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return success(baseService.selectInfoById(id));
    }

    /**
     * 企业权限组新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_AUTH_GROUP_ADD)")
    @Log(title = "企业权限组管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysAuthGroupDto authGroup) {
        return super.add(authGroup);
    }

    /**
     * 企业权限组修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_AUTH_GROUP_EDIT)")
    @Log(title = "企业权限组管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysAuthGroupDto authGroup) {
        return super.edit(authGroup);
    }

    /**
     * 企业权限组修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_AUTH_GROUP_EDIT, @Auth.SYS_AUTH_GROUP_ES)")
    @Log(title = "企业权限组管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysAuthGroupDto authGroup) {
        return super.editStatus(authGroup);
    }

    /**
     * 企业权限组批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_AUTH_GROUP_DEL)")
    @Log(title = "企业权限组管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

}
