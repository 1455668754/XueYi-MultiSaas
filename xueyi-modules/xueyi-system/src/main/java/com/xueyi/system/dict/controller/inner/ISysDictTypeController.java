package com.xueyi.system.dict.controller.inner;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.dict.controller.base.BSysDictTypeController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典类型管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/dict/type")
public class ISysDictTypeController extends BSysDictTypeController {

    /**
     * 同步字典缓存 | 租户数据
     *
     * @return 结果
     */
    @InnerAuth
    @GetMapping(value = "/sync")
    public R<Boolean> syncCacheInner() {
        return R.ok(baseService.syncCache());
    }

    /**
     * 刷新字典缓存 | 租户数据
     */
    @Override
    @InnerAuth
    @GetMapping("/refresh")
    @Log(title = "字典类型", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 刷新字典缓存 | 默认数据
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping("/common/refresh")
    @Log(title = "字典类型", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshTeCacheInner() {
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        return super.refreshCacheInner();
    }
}
