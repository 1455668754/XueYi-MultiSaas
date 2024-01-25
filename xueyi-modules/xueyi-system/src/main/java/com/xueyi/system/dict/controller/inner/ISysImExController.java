package com.xueyi.system.dict.controller.inner;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.dict.controller.base.BSysImExController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导入导出配置管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/imExConfig")
public class ISysImExController extends BSysImExController {

    /**
     * 同步配置缓存 | 租户数据
     *
     * @return 结果
     */
    @InnerAuth
    @GetMapping(value = "/sync")
    public R<Boolean> syncCacheInner() {
        return R.ok(baseService.syncCache());
    }

    /**
     * 刷新配置缓存 | 租户数据
     */
    @Override
    @InnerAuth
    @GetMapping("/refresh")
    @Log(title = "配置管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 刷新配置缓存 | 默认数据
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping("/common/refresh")
    @Log(title = "导入导出配置管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCommonCacheInner() {
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        return super.refreshCacheInner();
    }
}
