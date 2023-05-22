package com.xueyi.tenant.api.source.feign;

import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.feign.RemoteCacheService;
import com.xueyi.tenant.api.source.feign.factory.RemoteSourceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 数据源服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteSourceService", path = "/inner/source", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteSourceFallbackFactory.class)
public interface RemoteSourceService extends RemoteCacheService {
}