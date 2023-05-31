package com.xueyi.tenant.api.source.feign;

import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.feign.RemoteCacheService;
import com.xueyi.tenant.api.source.feign.factory.RemoteStrategyFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 租户服务 | 策略模块 | 源策略服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteStrategyService", path = "/inner/strategy", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteStrategyFallbackFactory.class)
public interface RemoteStrategyService extends RemoteCacheService {
}