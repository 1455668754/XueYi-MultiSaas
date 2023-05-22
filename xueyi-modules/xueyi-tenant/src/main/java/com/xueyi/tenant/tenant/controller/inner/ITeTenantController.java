package com.xueyi.tenant.tenant.controller.inner;

import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.tenant.tenant.controller.base.BTeTenantController;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理 远程业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/tenant")
public class ITeTenantController extends BTeTenantController {

    /**
     * 刷新源策略管理缓存
     */
    @Override
    @GetMapping("/refresh")
    @InnerAuth(isAnonymous = true)
    @Log(title = "租户管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 租户新增
     */
    @InnerAuth
    @PostMapping("/register")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_TENANT_ADD)")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    public R<Boolean> addInner(@Validated({V_A.class}) @RequestBody TeTenantRegister tenantRegister) {
        registerValidated(tenantRegister);
        return R.ok(baseService.insert(tenantRegister) > NumberUtil.Zero);
    }
}