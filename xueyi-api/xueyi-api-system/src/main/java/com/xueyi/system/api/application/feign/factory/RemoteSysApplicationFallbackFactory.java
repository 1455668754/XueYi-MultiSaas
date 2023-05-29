package com.xueyi.system.api.application.feign.factory;

import com.xueyi.common.core.constant.basic.AppConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.feign.RemoteSysApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 应用模块 | 应用服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteSysApplicationFallbackFactory implements FallbackFactory<RemoteSysApplicationService> {

    @Override
    public RemoteSysApplicationService create(Throwable throwable) {
        log.error("应用服务调用失败:{}", throwable.getMessage());
        return new RemoteSysApplicationService() {
            @Override
            public R<SysApplicationDto> getInfoByIdInner(Long id, AppConstants.AppType appType) {
                return R.fail("获取应用详细失败:" + throwable.getMessage());
            }

            @Override
            public R<SysApplicationDto> getInfoByAppIdInner(String id, AppConstants.AppType appType) {
                return R.fail("获取应用详细失败:" + throwable.getMessage());
            }
        };
    }
}