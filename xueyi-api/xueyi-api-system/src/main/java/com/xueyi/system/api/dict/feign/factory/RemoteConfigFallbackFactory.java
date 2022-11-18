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
            public R<String> getCode(String configCode) {
                return R.fail("获取参数失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshCache(String source) {
                return R.fail("刷新参数缓存失败:" + throwable.getMessage());
            }
        };
    }
}