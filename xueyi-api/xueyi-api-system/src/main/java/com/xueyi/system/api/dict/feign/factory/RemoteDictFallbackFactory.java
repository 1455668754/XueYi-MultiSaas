package com.xueyi.system.api.dict.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.dict.feign.RemoteDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 字典服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictService> {

    @Override
    public RemoteDictService create(Throwable throwable) {
        log.error("字典服务调用失败:{}", throwable.getMessage());
        return new RemoteDictService() {

            @Override
            public R<Boolean> syncCacheInner() {
                return R.fail("同步字典缓存失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshCacheInner() {
                return R.fail("刷新字典缓存失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshCommonCacheInner() {
                return R.fail("刷新字典缓存失败:" + throwable.getMessage());
            }
        };
    }
}