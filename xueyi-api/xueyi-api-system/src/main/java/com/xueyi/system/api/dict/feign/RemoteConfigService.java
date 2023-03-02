package com.xueyi.system.api.dict.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.dict.feign.factory.RemoteConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 参数服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteConfigService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteConfigFallbackFactory.class)
public interface RemoteConfigService {

    /**
     * 查询参数
     *
     * @param configCode 参数编码
     * @return 结果
     */
    @GetMapping("/config/innerCode/{configCode}")
    R<String> getCode(@PathVariable("configCode") String configCode);

    /**
     * 刷新参数缓存
     *
     * @return 结果
     */
    @GetMapping(value = "/config/inner/refresh", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> refreshCache();

}