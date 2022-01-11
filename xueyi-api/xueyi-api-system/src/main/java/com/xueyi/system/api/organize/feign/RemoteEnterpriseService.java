package com.xueyi.system.api.organize.feign;

import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.feign.factory.RemoteEnterpriseFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 企业服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteEnterpriseService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteEnterpriseFallbackFactory.class)
public interface RemoteEnterpriseService {

    /**
     * 根据租户Id查询企业信息
     *
     * @param enterpriseId 企业Id
     * @param source       请求来源
     * @return 结果
     */
    @GetMapping("/enterprise/infoById/{enterpriseId}")
    R<SysEnterpriseDto> enterpriseInfoById(@PathVariable("enterpriseId") Long enterpriseId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}