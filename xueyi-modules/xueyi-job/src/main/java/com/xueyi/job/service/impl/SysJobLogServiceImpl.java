package com.xueyi.job.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.job.domain.dto.SysJobLogDto;
import com.xueyi.job.manager.SysJobLogManager;
import com.xueyi.job.mapper.SysJobLogMapper;
import com.xueyi.job.service.ISysJobLogService;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 调度日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class SysJobLogServiceImpl extends BaseServiceImpl<SysJobLogDto, SysJobLogManager, SysJobLogMapper> implements ISysJobLogService {

    /**
     * 清空任务日志
     */
    @Override
    public void cleanLog() {
        baseManager.cleanLog();
    }
}
