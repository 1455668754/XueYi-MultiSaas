package com.xueyi.system.api.authority.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.feign.factory.RemoteClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 系统服务 | 权限模块 | 客户端认证服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteClientService", path = "/inner/client", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteClientFallbackFactory.class)
public interface RemoteClientService {

    /**
     * 查询客户端信息
     *
     * @param clientId 用户名
     * @return 客户端信息
     */
    @GetMapping(value = "/clientId", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysClientDto> getInfoByClientIdInner(@RequestParam("clientId") String clientId);

}