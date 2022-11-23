package com.xueyi.job.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.query.SysJobQuery;

import java.util.List;

/**
 * 调度任务管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysJobManager extends IBaseManager<SysJobQuery, SysJobDto> {

    /**
     * 项目启动时
     */
    List<SysJobDto> initScheduler();
}
