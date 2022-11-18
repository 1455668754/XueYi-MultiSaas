package com.xueyi.system.api.source.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.source.feign.RemoteSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 数据源策略加载服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteSourceFallbackFactory implements FallbackFactory<RemoteSourceService> {

    @Override
    public RemoteSourceService create(Throwable throwable) {
        log.error("数据源策略加载服务调用失败:{}", throwable.getMessage());
        return new RemoteSourceService() {
            @Override
            public R<Boolean> refreshSourceCache(Long strategyId, String source) {
                return R.fail("更新数据源策略失败:" + throwable.getMessage());
            }
        };
    }
}