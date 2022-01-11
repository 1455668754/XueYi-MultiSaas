package com.xueyi.common.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.xueyi.system.api.log.feign.RemoteLogService;
import com.xueyi.system.api.log.domain.dto.SysOperationLogDto;
import com.xueyi.common.core.constant.SecurityConstants;

/**
 * 异步调用日志服务
 *
 * @author xueyi
 */
@Service
public class AsyncLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperationLogDto sysOperationLogDto)
    {
        remoteLogService.saveLog(sysOperationLogDto, SecurityConstants.INNER);
    }
}