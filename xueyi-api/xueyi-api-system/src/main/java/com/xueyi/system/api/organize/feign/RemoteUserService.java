package com.xueyi.system.api.organize.feign;

import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.feign.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 用户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    /**
     * 新增用户
     *
     * @param user 用户对象
     * @return 结果
     */
    @PostMapping("/user/inner/add")
    R<SysUserDto> add(@RequestBody SysUserDto user, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}