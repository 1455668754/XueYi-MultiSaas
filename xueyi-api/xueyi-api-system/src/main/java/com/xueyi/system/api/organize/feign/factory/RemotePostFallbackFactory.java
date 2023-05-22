package com.xueyi.system.api.organize.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.feign.RemotePostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 岗位服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemotePostFallbackFactory implements FallbackFactory<RemotePostService> {

    @Override
    public RemotePostService create(Throwable throwable) {
        log.error("岗位服务调用失败:{}", throwable.getMessage());
        return new RemotePostService() {
            @Override
            public R<SysPostDto> addInner(SysPostDto post, Long enterpriseId, String sourceName) {
                return R.fail("新增岗位失败:" + throwable.getMessage());
            }
        };
    }
}