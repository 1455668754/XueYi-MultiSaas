package com.xueyi.system.api.feign.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.domain.organize.dto.SysEnterpriseDto;
import com.xueyi.system.api.feign.RemoteEnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 企业服务 降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteEnterpriseFallbackFactory implements FallbackFactory<RemoteEnterpriseService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteEnterpriseFallbackFactory.class);

    @Override
    public RemoteEnterpriseService create(Throwable throwable) {
        log.error("企业服务调用失败:{}", throwable.getMessage());
        return new RemoteEnterpriseService() {
            @Override
            public R<SysEnterpriseDto> enterpriseInfoById(Long enterpriseId, String source) {
                return R.fail("获取企业失败:" + throwable.getMessage());
            }
        };
    }
}