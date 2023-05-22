package com.xueyi.tenant.tenant.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.tenant.controller.base.BTeTenantController;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import jakarta.servlet.http.HttpServletResponse;
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
 * 租户管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/tenant")
public class ATeTenantController extends BTeTenantController {

    /**
     * 查询租户列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_LIST)")
    public AjaxResult list(TeTenantQuery tenant) {
        return super.list(tenant);
    }

    /**
     * 查询租户详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询租户权限
     */
    @GetMapping("/auth/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_AUTH)")
    public AjaxResult getAuth(@PathVariable Long id) {
        return success(baseService.selectAuth(id));
    }

    /**
     * 租户导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_EXPORT)")
    public void export(HttpServletResponse response, TeTenantQuery tenant) {
        super.export(response, tenant);
    }

    /**
     * 租户新增
     */
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_ADD)")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody TeTenantRegister tenantRegister) {
        registerValidated(tenantRegister);
        return toAjax(baseService.insert(tenantRegister));
    }

    /**
     * 租户修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_EDIT)")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody TeTenantDto tenant) {
        return super.edit(tenant);
    }

    /**
     * 租户权限修改
     */
    @PutMapping("/auth")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_AUTH)")
    @Log(title = "租户管理", businessType = BusinessType.AUTH)
    public AjaxResult editAuth(@RequestBody TeTenantDto tenant) {
        baseService.updateAuth(tenant.getId(), tenant.getAuthIds());
        return success();
    }

    /**
     * 租户修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.TE_TENANT_EDIT, @Auth.TE_TENANT_ES)")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody TeTenantDto tenant) {
        return super.editStatus(tenant);
    }

    /**
     * 租户批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_DEL)")
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

}