package com.xueyi.tenant.source.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.tenant.source.controller.base.BTeSourceController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户服务 | 策略模块 | 数据源管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/source")
public class ITeSourceController extends BTeSourceController {

    /**
     * 刷新数据源缓存 | 内部调用
     */
    @Override
    @InnerAuth(isAnonymous = true)
    @GetMapping("/refresh")
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

}