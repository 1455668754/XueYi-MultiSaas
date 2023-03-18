package com.xueyi.system.api.dict.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
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
     * 查询参数对象
     *
     * @param code 参数编码
     * @return 参数对象
     */
    @GetMapping(value = "/config/inner/code/{code}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysConfigDto> getConfigByCodeInner(@PathVariable("code") String code);

    /**
     * 查询参数
     *
     * @param code 参数编码
     * @return 参数
     */
    @GetMapping(value = "/config/inner/value/{code}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<String> getValueByCodeInner(@PathVariable("code") String code);

    /**
     * 刷新参数缓存
     *
     * @return 结果
     */
    @GetMapping(value = "/config/inner/refresh", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> refreshCacheInner();

}