package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import com.xueyi.system.authority.controller.base.BSysRoleController;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 系统服务 | 权限模块 | 角色管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/role")
public class ASysRoleController extends BSysRoleController {

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 查询角色列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_ROLE_LIST, @Auth.SYS_DEPT_LIST, @Auth.SYS_POST_LIST, @Auth.SYS_USER_LIST)")
    public AjaxResult list(SysRoleQuery role) {
        return super.list(role);
    }

    /**
     * 查询角色详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 获取角色功能权限 | 叶子节点
     */
    @GetMapping("/auth")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_AUTH)")
    public AjaxResult getRoleAuth(@RequestParam Long id) {
        return success(baseService.selectAuthById(id));
    }

    /**
     * 获取角色组织权限
     */
    @GetMapping("/organize")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_AUTH)")
    public AjaxResult getRoleOrganize(@RequestParam Long id) {
        return success(organizeService.selectRoleOrganizeMerge(id));
    }

    /**
     * 角色新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_ADD)")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysRoleDto role) {
        return super.add(role);
    }

    /**
     * 角色修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_EDIT)")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysRoleDto role) {
        return super.edit(role);
    }

    /**
     * 角色修改功能权限
     */
    @PutMapping("/auth")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_AUTH)")
    @Log(title = "角色管理", businessType = BusinessType.AUTH)
    public AjaxResult editAuth(@RequestBody SysRoleDto role) {
        baseService.editRoleAuth(role);
        return success();
    }

    /**
     * 角色修改组织权限
     */
    @PutMapping("/organize")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_AUTH)")
    @Log(title = "角色管理", businessType = BusinessType.AUTH)
    public AjaxResult editOrganize(@RequestBody SysRoleDto role) {
        baseService.updateDataScope(role);
        return success();
    }

    /**
     * 角色修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_ROLE_EDIT, @Auth.SYS_ROLE_ES)")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysRoleDto role) {
        return super.editStatus(role);
    }

    /**
     * 角色批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ROLE_DEL)")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

}
