package com.xueyi.job.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.job.domain.dto.SysJobLogDto;
import com.xueyi.job.service.ISysJobLogService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 调度日志管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/job/log")
public class SysJobLogController extends BaseController<SysJobLogDto, ISysJobLogService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "调度日志";
    }

    /**
     * 查询调度日志列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SCHEDULE_JOB_SINGLE)
    public AjaxResult list(SysJobLogDto jobLog) {
        return super.list(jobLog);
    }

    /**
     * 调度日志导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SCHEDULE_JOB_EXPORT)
    @Log(title = "调度日志管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysJobLogDto jobLog) {
        super.export(response, jobLog);
    }

    /**
     * 清空调度日志
     */
    @RequiresPermissions(Auth.SCHEDULE_JOB_DELETE)
    @Log(title = "调度日志管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        baseService.cleanLog();
        return AjaxResult.success();
    }
}
