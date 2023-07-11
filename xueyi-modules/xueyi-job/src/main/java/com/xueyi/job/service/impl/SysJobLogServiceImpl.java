package com.xueyi.job.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import com.xueyi.job.domain.correlate.SysJobLogCorrelate;
import com.xueyi.job.manager.impl.SysJobLogManagerImpl;
import com.xueyi.job.service.ISysJobLogService;
import org.springframework.stereotype.Service;

/**
 * 调度日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysJobLogServiceImpl extends BaseServiceImpl<SysJobLogQuery, SysJobLogDto, SysJobLogCorrelate, SysJobLogManagerImpl> implements ISysJobLogService {

    /**
     * 清空任务日志
     */
    @Override
    public void cleanLog() {
        baseManager.cleanLog();
    }
}
