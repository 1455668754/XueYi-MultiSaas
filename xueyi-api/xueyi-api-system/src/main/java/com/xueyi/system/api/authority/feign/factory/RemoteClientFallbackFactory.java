package com.xueyi.system.api.authority.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.feign.RemoteClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客户端认证服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteClientFallbackFactory implements FallbackFactory<RemoteClientService> {

    @Override
    public RemoteClientService create(Throwable throwable) {
        log.error("客户端认证服务调用失败:{}", throwable.getMessage());
        return new RemoteClientService() {
            @Override
            public R<SysClientDto> getInfoByClientIdInner(String clientId) {
                return R.fail("获取客户端信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysClientDto>> getListInner() {
                return R.fail("新增客户端信息失败:" + throwable.getMessage());
            }

        };
    }
}