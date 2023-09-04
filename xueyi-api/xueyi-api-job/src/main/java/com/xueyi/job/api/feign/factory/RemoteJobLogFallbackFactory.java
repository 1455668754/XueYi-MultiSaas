package com.xueyi.job.api.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.feign.RemoteJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 调度日志服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteJobLogFallbackFactory implements FallbackFactory<RemoteJobLogService> {

    @Override
    public RemoteJobLogService create(Throwable throwable) {
        log.error("调度日志服务调用失败:{}", throwable.getMessage());
        return new RemoteJobLogService() {
            @Override
            public R<Boolean> saveJobLog(SysJobLogDto jobLog, Long enterpriseId, String isLessor, String sourceName) {
                return R.fail("调度日志保存失败");
            }
        };
    }
}