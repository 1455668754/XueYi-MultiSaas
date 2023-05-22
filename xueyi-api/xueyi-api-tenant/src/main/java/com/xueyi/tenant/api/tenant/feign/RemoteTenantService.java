package com.xueyi.tenant.api.tenant.feign;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.feign.RemoteCacheService;
import com.xueyi.common.core.web.result.R;
import com.xueyi.tenant.api.tenant.feign.factory.RemoteTenantFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 租户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteTenantService", path = "/inner/tenant", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteTenantFallbackFactory.class)
public interface RemoteTenantService extends RemoteCacheService {

    /**
     * 注册租户信息
     *
     * @param register 注册信息 | 约定json内容tenant = tenant, dept = dept, post = post, user = user
     * @return 结果
     */
    @PostMapping(value = "/register", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> registerTenantInfo(@RequestBody JSONObject register);
}