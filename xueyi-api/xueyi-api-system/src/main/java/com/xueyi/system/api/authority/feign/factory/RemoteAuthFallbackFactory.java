package com.xueyi.system.api.authority.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.RemoteAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 权限模块 | 权限服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteAuthFallbackFactory implements FallbackFactory<RemoteAuthService> {

    @Override
    public RemoteAuthService create(Throwable throwable) {
        log.error("权限服务调用失败:{}", throwable.getMessage());
        return new RemoteAuthService() {
            @Override
            public R<Long[]> getTenantAuthInner(Long enterpriseId, String sourceName, String source) {
                return R.fail("获取租户权限信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> addTenantAuthInner(Long[] authIds, Long enterpriseId, String sourceName, String source) {
                return R.fail("新增租户权限信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> editTenantAuthInner(Long[] authIds, Long enterpriseId, String sourceName, String source) {
                return R.fail("修改权限信息失败:" + throwable.getMessage());
            }
        };
    }
}