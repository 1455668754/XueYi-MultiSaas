package com.xueyi.system.monitor.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.domain.monitor.dto.SysOperationLogDto;

/**
 * 操作日志管理 服务层
 *
 * @author xueyi
 */
public interface ISysOperationLogService extends IBaseService<SysOperationLogDto> {

    /**
     * 清空操作日志
     */
    void cleanOperationLog();
}
