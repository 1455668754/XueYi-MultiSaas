package com.xueyi.job.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.job.domain.dto.SysJobLogDto;
import com.xueyi.job.manager.SysJobLogManager;
import com.xueyi.job.mapper.SysJobLogMapper;
import com.xueyi.job.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 调度任务日志管理 服务层实现
 *
 * @author xueyi
 */
@Service
public class SysJobLogServiceImpl extends BaseServiceImpl<SysJobLogDto, SysJobLogManager, SysJobLogMapper> implements ISysJobLogService {

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog()
    {
        baseManager.cleanJobLog();
    }
}
