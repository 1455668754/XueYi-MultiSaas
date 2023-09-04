package com.xueyi.job.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import com.xueyi.job.controller.base.BSysJobLogController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 定时任务 | 调度日志管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/job/log")
public class ASysJobLogController extends BSysJobLogController {

    /**
     * 查询调度日志列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_LIST)")
    public AjaxResult list(SysJobLogQuery jobLog) {
        return super.list(jobLog);
    }

    /**
     * 查询调度日志详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 清空调度日志
     */
    @DeleteMapping("/clean")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_DEL)")
    @Log(title = "调度日志管理", businessType = BusinessType.CLEAN)
    public AjaxResult clean() {
        baseService.cleanLog();
        return success();
    }
}
