package com.xueyi.system.monitor.controller.base;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import com.xueyi.system.monitor.service.ISysOperateLogService;

/**
 * 系统服务 | 监控模块 | 操作日志管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysOperateLogController extends BaseController<SysOperateLogQuery, SysOperateLogDto, ISysOperateLogService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "操作日志";
    }

}
