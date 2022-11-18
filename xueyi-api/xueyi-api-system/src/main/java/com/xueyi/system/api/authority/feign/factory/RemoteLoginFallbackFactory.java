package com.xueyi.system.api.authority.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.RemoteLoginService;
import com.xueyi.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 登录服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteLoginFallbackFactory implements FallbackFactory<RemoteLoginService> {

    @Override
    public RemoteLoginService create(Throwable throwable) {
        log.error("登录服务调用失败:{}", throwable.getMessage());
        return new RemoteLoginService() {
            @Override
            public R<LoginUser> getLoginInfoInner(String enterpriseName, String userName, String password, String source) {
                return R.fail("获取登录信息失败:" + throwable.getMessage());
            }
        };
    }
}
