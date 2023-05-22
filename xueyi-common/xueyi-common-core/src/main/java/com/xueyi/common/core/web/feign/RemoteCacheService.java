package com.xueyi.common.core.web.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.web.result.R;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程缓存服务层
 *
 * @author xueyi
 */
public interface RemoteCacheService {

    /**
     * 刷新缓存
     *
     * @return 结果
     */
    @GetMapping(value = "/refresh", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> refreshCacheInner();
}
