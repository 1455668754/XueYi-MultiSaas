package com.xueyi.system.api.organize.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.feign.RemoteSelectService;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.feign.factory.RemoteDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 系统服务 | 组织模块 | 部门服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteDeptService", path = "/inner/dept", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteDeptFallbackFactory.class)
public interface RemoteDeptService extends RemoteSelectService<SysDeptDto> {

    /**
     * 新增部门
     *
     * @param dept         部门对象
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 结果
     */
    @PostMapping(headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysDeptDto> addInner(@RequestBody SysDeptDto dept, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName);

}