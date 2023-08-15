package com.xueyi.job.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import com.xueyi.job.service.ISysJobLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 调度日志管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/job/log")
public class SysJobLogController extends BaseController<SysJobLogQuery, SysJobLogDto, ISysJobLogService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "调度日志";
    }

    /**
     * 新增调度日志 | 内部调用
     */
    @InnerAuth
    @PostMapping
    public R<Boolean> addInner(@RequestBody SysJobLogDto jobLog) {
        baseService.insert(jobLog);
        return R.ok();
    }

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
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_SINGLE)")
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
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
