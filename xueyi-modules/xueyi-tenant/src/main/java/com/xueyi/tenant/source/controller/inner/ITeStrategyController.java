package com.xueyi.tenant.source.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.tenant.source.controller.base.BTeStrategyController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 源策略管理 远程业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/strategy")
public class ITeStrategyController extends BTeStrategyController {

    /**
     * 刷新源策略管理缓存 | 内部调用
     */
    @Override
    @InnerAuth(isAnonymous = true)
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    @GetMapping("/refresh")
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }
}