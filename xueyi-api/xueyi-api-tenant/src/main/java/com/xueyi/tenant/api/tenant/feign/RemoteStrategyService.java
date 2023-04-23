package com.xueyi.tenant.api.tenant.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.tenant.feign.factory.RemoteStrategyFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 源策略服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteStrategyService", path = "/inner/strategy", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteStrategyFallbackFactory.class)
public interface RemoteStrategyService {

    /**
     * 刷新源策略缓存
     *
     * @return 结果
     */
    @GetMapping(value = "/refresh", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> refreshCacheInner();
}