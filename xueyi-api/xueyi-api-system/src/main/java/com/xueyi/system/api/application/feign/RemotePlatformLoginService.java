package com.xueyi.system.api.application.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.application.feign.factory.RemotePlatformLoginFallbackFactory;
import com.xueyi.system.api.model.LoginPlatform;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 登录服务 | 平台端
 *
 * @author xueyi
 */
@FeignClient(contextId = "remotePlatformLoginService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemotePlatformLoginFallbackFactory.class)
public interface RemotePlatformLoginService {

    /**
     * 查询登录信息
     *
     * @param appId        应用Id
     * @return 结果
     */
    @GetMapping(value = "/platform/login/inner/loginInfo/{appId}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<LoginPlatform> getLoginInfoInner(@PathVariable("appId") String appId);

    /**
     * 查询登录信息
     *
     * @param appId        应用Id
     * @param enterpriseId 企业Id
     * @return 结果
     */
    @GetMapping(value = "/platform/login/inner/loginInfo/{appId}/{enterpriseId}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<LoginPlatform> getLoginInfoInner(@PathVariable("appId") String appId, @PathVariable("enterpriseId") Long enterpriseId);

}