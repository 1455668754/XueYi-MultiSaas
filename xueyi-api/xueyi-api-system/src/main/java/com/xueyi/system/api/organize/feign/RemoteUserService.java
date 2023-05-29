package com.xueyi.system.api.organize.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.feign.RemoteSelectService;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.feign.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 系统服务 | 组织模块 | 用户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteUserService", path = "/inner/user", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService extends RemoteSelectService<SysUserDto> {

    /**
     * 新增用户
     *
     * @param user         用户对象
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 结果
     */
    @PostMapping(value = "/add", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysUserDto> addInner(@RequestBody SysUserDto user, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName);
}