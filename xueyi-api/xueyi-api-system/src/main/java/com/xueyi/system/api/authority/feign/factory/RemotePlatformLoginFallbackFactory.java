package com.xueyi.system.api.authority.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.RemotePlatformLoginService;
import com.xueyi.system.api.model.LoginPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 登录服务 | 平台端 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemotePlatformLoginFallbackFactory implements FallbackFactory<RemotePlatformLoginService> {

    @Override
    public RemotePlatformLoginService create(Throwable throwable) {
        log.error("登录服务调用失败:{}", throwable.getMessage());
        return new RemotePlatformLoginService() {

            @Override
            public R<LoginPlatform> getLoginInfoInner(String appId) {
                return R.fail("获取登录信息失败:" + throwable.getMessage());
            }

            @Override
            public R<LoginPlatform> getLoginInfoInner(String appId, Long enterpriseId) {
                return R.fail("获取登录信息失败:" + throwable.getMessage());
            }
        };
    }
}
