package com.xueyi.job.controller.base;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import com.xueyi.job.service.ISysJobLogService;

/**
 * 定时任务 | 调度日志管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysJobLogController extends BaseController<SysJobLogQuery, SysJobLogDto, ISysJobLogService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "调度日志";
    }
}
