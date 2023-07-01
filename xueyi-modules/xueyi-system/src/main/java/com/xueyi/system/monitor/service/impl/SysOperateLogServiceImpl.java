package com.xueyi.system.monitor.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import com.xueyi.system.monitor.domain.correlate.SysOperateLogCorrelate;
import com.xueyi.system.monitor.manager.ISysOperateLogManager;
import com.xueyi.system.monitor.service.ISysOperateLogService;
import org.springframework.stereotype.Service;

/**
 * 系统服务 | 监控模块 | 操作日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysOperateLogServiceImpl extends BaseServiceImpl<SysOperateLogQuery, SysOperateLogDto, SysOperateLogCorrelate, ISysOperateLogManager> implements ISysOperateLogService {

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperateLog() {
        baseManager.cleanOperateLog();
    }
}
