package com.xueyi.system.api.feign.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.feign.RemoteLogService;
import com.xueyi.system.api.domain.monitor.dto.SysLoginLogDto;
import com.xueyi.system.api.domain.monitor.dto.SysOperationLogDto;

/**
 * 日志服务 降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public R<Boolean> saveLog(SysOperationLogDto operationLog, String source) {
                return null;
            }

            @Override
            public R<Boolean> saveLoginInfo(SysLoginLogDto loginInfo, String source) {
                return null;
            }
        };
    }
}