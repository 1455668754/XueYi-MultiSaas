package com.xueyi.tenant.tenant.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 租户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TeTenantController extends BaseController<TeTenantQuery, TeTenantDto, ITeTenantService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "租户";
    }

    /**
     * 租户新增 | 内部调用
     */
    @InnerAuth
    @PostMapping("/register")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_ADD)")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    public AjaxResult addInner(@Validated({V_A.class}) @RequestBody TeTenantRegister tenantRegister) {
        return add(tenantRegister);
    }

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

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, TeTenantDto tenant) {
        if (baseService.checkNameUnique(tenant.getId(), tenant.getName()))
            warn(StrUtil.format("{}{}{}失败，{}名称已存在", operate.getInfo(), getNodeName(), tenant.getName(), getNodeName()));
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--)
            if (baseService.checkIsDefault(idList.get(i)))
                idList.remove(i);
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("删除失败，默认{}不允许删除！", getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            warn(StrUtil.format("默认{}不允许删除，其余{}删除成功！", getNodeName(), getNodeName()));
        }
    }

    /**
     * 租户新增/注册校验
     */
    private void registerValidated(TeTenantRegister tenantRegister) {
        String enterpriseName = tenantRegister.getTenant().getName();
        String userName = tenantRegister.getUser().getUserName();
        String password = tenantRegister.getUser().getPassword();
        if (StrUtil.isBlank(enterpriseName))
            warn("企业账号必须填写");
        else if (enterpriseName.length() < OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH || enterpriseName.length() > OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH)
            warn("企业账号长度必须在" + OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH + "到" + OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH + "个字符之间");
        else if (StrUtil.hasBlank(userName, password))
            warn("用户账号/密码必须填写");
        else if (userName.length() < OrganizeConstants.USERNAME_MIN_LENGTH || userName.length() > OrganizeConstants.USERNAME_MAX_LENGTH)
            warn("用户账号长度必须在" + OrganizeConstants.USERNAME_MIN_LENGTH + "到" + OrganizeConstants.USERNAME_MAX_LENGTH + "个字符之间");
        else if (password.length() < OrganizeConstants.PASSWORD_MIN_LENGTH || password.length() > OrganizeConstants.PASSWORD_MAX_LENGTH)
            warn("用户密码长度必须在" + OrganizeConstants.PASSWORD_MIN_LENGTH + "到" + OrganizeConstants.PASSWORD_MAX_LENGTH + "个字符之间");
        else if (baseService.checkNameUnique(tenantRegister.getTenant().getId(), tenantRegister.getTenant().getName()))
            warn("企业账号已存在，请更换后再提交！");
    }
}