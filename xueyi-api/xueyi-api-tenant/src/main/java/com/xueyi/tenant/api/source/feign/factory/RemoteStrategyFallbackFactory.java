package com.xueyi.tenant.api.source.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.source.feign.RemoteStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 租户服务 | 策略模块 | 源策略服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteStrategyFallbackFactory implements FallbackFactory<RemoteStrategyService> {

    @Override
    public RemoteStrategyService create(Throwable throwable) {
        log.error("源策略服务调用失败:{}", throwable.getMessage());
        return () -> R.fail("刷新源策略缓存失败:" + throwable.getMessage());
    }
}