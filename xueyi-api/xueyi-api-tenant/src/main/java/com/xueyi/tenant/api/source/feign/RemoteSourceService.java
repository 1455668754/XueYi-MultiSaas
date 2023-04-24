package com.xueyi.tenant.api.source.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.source.feign.factory.RemoteSourceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 数据源服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteSourceService", path = "/inner/source", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteSourceFallbackFactory.class)
public interface RemoteSourceService {

    /**
     * 刷新参数缓存
     *
     * @return 结果
     */
    @GetMapping(value = "/refresh", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> refreshCacheInner();
}