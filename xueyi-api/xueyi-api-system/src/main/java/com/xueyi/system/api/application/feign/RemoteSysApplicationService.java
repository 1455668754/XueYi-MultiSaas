package com.xueyi.system.api.application.feign;

import com.xueyi.common.core.constant.basic.AppConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.feign.factory.RemoteSysApplicationFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统服务 | 应用模块 | 应用服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteSysApplicationService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysApplicationFallbackFactory.class)
public interface RemoteSysApplicationService {

    /**
     * 查询应用详细 | By应用Id
     *
     * @param id 应用Id
     * @return 结果
     */
    @GetMapping(value = "/application/inner/{id}/{appType}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysApplicationDto> getInfoByIdInner(@PathVariable("id") Long id, @PathVariable("appType") AppConstants.AppType appType);

    /**
     * 查询应用详细 | By应用AppId
     *
     * @param appId 应用AppId
     * @return 结果
     */
    @GetMapping(value = "/application/inner/app/{appId}/{appType}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysApplicationDto> getInfoByAppIdInner(@PathVariable("appId") String appId, @PathVariable("appType") AppConstants.AppType appType);
}