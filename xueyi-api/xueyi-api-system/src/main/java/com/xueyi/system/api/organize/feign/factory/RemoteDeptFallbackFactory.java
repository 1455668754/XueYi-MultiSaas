package com.xueyi.system.api.organize.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.feign.RemoteDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 部门服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteDeptFallbackFactory implements FallbackFactory<RemoteDeptService> {

    @Override
    public RemoteDeptService create(Throwable throwable) {
        log.error("部门服务调用失败:{}", throwable.getMessage());
        return new RemoteDeptService() {
            @Override
            public R<SysDeptDto> addInner(SysDeptDto dept, Long enterpriseId, String sourceName, String source) {
                return R.fail("新增部门失败:" + throwable.getMessage());
            }
        };
    }
}