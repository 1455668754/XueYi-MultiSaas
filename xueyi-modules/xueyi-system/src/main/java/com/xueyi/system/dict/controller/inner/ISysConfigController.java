package com.xueyi.system.dict.controller.inner;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.dict.controller.base.BSysConfigController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/config")
public class ISysConfigController extends BSysConfigController {

    /**
     * 同步参数缓存 | 租户数据
     *
     * @return 结果
     */
    @InnerAuth
    @GetMapping(value = "/sync")
    public R<Boolean> syncCacheInner() {
        return R.ok(baseService.syncCache());
    }

    /**
     * 刷新参数缓存 | 租户数据
     */
    @Override
    @InnerAuth
    @GetMapping("/refresh")
    @Log(title = "参数管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 刷新参数缓存 | 默认数据
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping("/common/refresh")
    @Log(title = "参数管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshTeCacheInner() {
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        return super.refreshCacheInner();
    }
}
