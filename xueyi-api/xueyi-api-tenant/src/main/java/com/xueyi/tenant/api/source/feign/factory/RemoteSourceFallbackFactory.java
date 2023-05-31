package com.xueyi.tenant.api.source.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.source.feign.RemoteSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 租户服务 | 策略模块 | 数据源服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteSourceFallbackFactory implements FallbackFactory<RemoteSourceService> {

    @Override
    public RemoteSourceService create(Throwable throwable) {
        log.error("数据源服务调用失败:{}", throwable.getMessage());
        return () -> R.fail("刷新数据源缓存失败:" + throwable.getMessage());
    }
}