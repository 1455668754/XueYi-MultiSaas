package com.xueyi.system.api.dict.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.feign.RemoteCacheService;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.dict.feign.factory.RemoteConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 系统服务 | 字典模块 | 参数服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteConfigService", path = "/inner/config", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteConfigFallbackFactory.class)
public interface RemoteConfigService extends RemoteCacheService {

    /**
     * 同步参数缓存 | 租户数据
     *
     * @return 结果
     */
    @GetMapping(value = "/sync", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> syncCacheInner();

    /**
     * 刷新参数缓存 | 默认数据
     *
     * @return 结果
     */
    @GetMapping(value = "/common/refresh", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> refreshTeCacheInner();
}