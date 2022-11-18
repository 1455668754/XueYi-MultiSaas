package com.xueyi.system.api.dict.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.dict.feign.factory.RemoteDictFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 参数服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteDictService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteDictFallbackFactory.class)
public interface RemoteDictService {

    /**
     * 刷新参数缓存
     *
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/dict/type/inner/refresh")
    R<Boolean> refreshCache(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}