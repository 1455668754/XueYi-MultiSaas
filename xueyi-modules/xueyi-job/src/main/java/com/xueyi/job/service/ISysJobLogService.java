package com.xueyi.job.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.job.domain.dto.SysJobLogDto;

import java.util.List;

/**
 * 调度任务日志管理 服务层
 *
 * @author xueyi
 */
public interface ISysJobLogService extends IBaseService<SysJobLogDto> {

    /**
     * 清空任务日志
     */
    public void cleanJobLog();
}
