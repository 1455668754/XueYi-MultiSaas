package com.xueyi.tenant.tenant.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.OrganizeConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 租户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TeTenantController extends BaseController<TeTenantDto, ITeTenantService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "租户";
    }

    /**
     * 查询租户列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.TE_TENANT_LIST)
    public AjaxResult listExtra(TeTenantDto tenant) {
        return super.listExtra(tenant);
    }

    /**
     * 查询租户详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.TE_TENANT_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 租户导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.TE_TENANT_EXPORT)
    public void export(HttpServletResponse response, TeTenantDto tenant) {
        super.export(response, tenant);
    }

    /**
     * 租户新增
     */
    @PostMapping
    @RequiresPermissions(Auth.TE_TENANT_ADD)
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody TeTenantRegister tenantRegister) {

        return toAjax(baseService.insert(tenantRegister));
    }

    /**
     * 租户修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.TE_TENANT_EDIT)
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody TeTenantDto tenant) {
        return super.edit(tenant);
    }

    /**
     * 租户修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.TE_TENANT_EDIT, Auth.TE_TENANT_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "租户管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@Validated @RequestBody TeTenantDto tenant) {
        return super.editStatus(tenant);
    }

    /**
     * 租户批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.TE_TENANT_DELETE)
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void baseRemoveValidated(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--)
            if (baseService.checkIsDefault(idList.get(i)))
                idList.remove(i);
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException(StrUtil.format("删除失败，默认{}不允许删除！", getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            throw new ServiceException(StrUtil.format("默认{}不允许删除，其余{}删除成功！", getNodeName(), getNodeName()));
        }
    }

    /**
     * 租户新增/注册校验
     */
    private void registerValidated(TeTenantRegister tenantRegister){
        String enterpriseName = tenantRegister.getTenant().getName();
        String userName = tenantRegister.getUser().getUserName();
        String password = tenantRegister.getUser().getPassword();
        // 企业账号为空 错误
        if (StringUtils.isBlank(enterpriseName)) {
            throw new ServiceException("企业账号必须填写");
        }
        if (enterpriseName.length() < OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH
                || enterpriseName.length() > OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH) {
            throw new ServiceException("企业账号长度必须在" + OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH + "到" + OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH + "个字符之间");
        }

        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(userName, password)) {
            throw new ServiceException("用户账号/密码必须填写");
        }
        if (userName.length() < OrganizeConstants.USERNAME_MIN_LENGTH
                || userName.length() > OrganizeConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("用户账号长度必须在" + OrganizeConstants.USERNAME_MIN_LENGTH + "到" + OrganizeConstants.USERNAME_MAX_LENGTH + "个字符之间");
        }
        if (password.length() < OrganizeConstants.PASSWORD_MIN_LENGTH
                || password.length() > OrganizeConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("用户密码长度必须在" + OrganizeConstants.PASSWORD_MIN_LENGTH + "到" + OrganizeConstants.PASSWORD_MAX_LENGTH + "个字符之间");
        }
    }
}