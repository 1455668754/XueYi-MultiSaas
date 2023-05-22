package com.xueyi.system.organize.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.organize.controller.base.BSysDeptController;
import com.xueyi.system.organize.service.ISysOrganizeService;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 部门管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/dept")
public class ASysDeptController extends BSysDeptController {

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 查询部门列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_LIST)")
    public AjaxResult list(SysDeptQuery dept) {
        return super.list(dept);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @Override
    @GetMapping("/list/exclude")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_LIST)")
    public AjaxResult listExNodes(SysDeptQuery dept) {
        return super.listExNodes(dept);
    }

    /**
     * 查询部门详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询部门关联的角色Id集
     */
    @GetMapping(value = "/auth/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_AUTH)")
    public AjaxResult getRoleAuth(@PathVariable Long id) {
        return success(organizeService.selectDeptRoleMerge(id));
    }

    /**
     * 部门导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_EXPORT)")
    @Log(title = "部门管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysDeptQuery dept) {
        super.export(response, dept);
    }

    /**
     * 部门新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_ADD)")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysDeptDto dept) {
        return super.add(dept);
    }

    /**
     * 部门修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_EDIT)")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysDeptDto dept) {
        return super.edit(dept);
    }

    /**
     * 查询部门关联的角色Id集
     */
    @PutMapping(value = "/auth")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_AUTH)")
    public AjaxResult editRoleAuth(@RequestBody SysDeptDto dept) {
        organizeService.editDeptRoleMerge(dept.getId(), dept.getRoleIds());
        return success();
    }

    /**
     * 部门修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_DEPT_EDIT, @Auth.SYS_DEPT_ES)")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysDeptDto dept) {
        return super.editStatus(dept);
    }

    /**
     * 部门批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DEPT_DEL)")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取部门选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

}
