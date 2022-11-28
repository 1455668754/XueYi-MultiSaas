package com.xueyi.tenant.api.tenant.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.tenant.feign.factory.RemoteStrategyFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 源策略服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteStrategyService", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteStrategyFallbackFactory.class)
public interface RemoteStrategyService {

    /**
     * 刷新源策略缓存
     *
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/strategy/inner/refresh")
    R<Boolean> refreshCache(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}