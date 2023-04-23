package com.xueyi.tenant.api.tenant.feign.factory;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.tenant.feign.RemoteTenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 租户服务降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteTenantFallbackFactory implements FallbackFactory<RemoteTenantService> {

    @Override
    public RemoteTenantService create(Throwable throwable) {
        log.error("租户服务调用失败:{}", throwable.getMessage());
        return new RemoteTenantService() {

            @Override
            public R<Boolean> refreshCacheInner() {
                return R.fail("刷新租户缓存失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerTenantInfo(JSONObject register) {
                return R.fail("注册租户失败:" + throwable.getMessage());
            }
        };
    }
}