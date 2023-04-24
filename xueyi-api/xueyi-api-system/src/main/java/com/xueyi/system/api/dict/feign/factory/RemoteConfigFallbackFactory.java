package com.xueyi.system.api.dict.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 参数服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteConfigFallbackFactory implements FallbackFactory<RemoteConfigService> {

    @Override
    public RemoteConfigService create(Throwable throwable) {
        log.error("参数服务调用失败:{}", throwable.getMessage());
        return new RemoteConfigService() {

            @Override
            public R<Boolean> syncCacheInner() {
                return R.fail("同步参数缓存失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshCacheInner() {
                return R.fail("刷新参数缓存失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshTeCacheInner() {
                return R.fail("刷新参数缓存失败:" + throwable.getMessage());
            }
        };
    }
}