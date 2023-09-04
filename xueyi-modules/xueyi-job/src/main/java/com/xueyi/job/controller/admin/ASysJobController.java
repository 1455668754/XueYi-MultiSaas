package com.xueyi.job.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.query.SysJobQuery;
import com.xueyi.job.controller.base.BSysJobController;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 定时任务 | 调度任务管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/job")
public class ASysJobController extends BSysJobController {

    /**
     * 查询定时任务列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_LIST)")
    public AjaxResult list(SysJobQuery job) {
        return super.list(job);
    }

    /**
     * 查询调度任务详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 调度任务新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_ADD)")
    @Log(title = "调度任务管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysJobDto job) {
        return super.add(job);
    }

    /**
     * 调度任务修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_EDIT)")
    @Log(title = "调度任务管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysJobDto job) {
        return super.edit(job);
    }

    /**
     * 调度任务修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SCHEDULE_JOB_EDIT, @Auth.SCHEDULE_JOB_ES)")
    @Log(title = "调度任务管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysJobDto job) {
        return super.editStatus(job);
    }

    /**
     * 定时任务立即执行一次
     */
    @GetMapping("/run/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_EDIT)")
    @Log(title = "定时任务 - 执行一次", businessType = BusinessType.UPDATE)
    public AjaxResult run(@PathVariable Long id) throws SchedulerException {
        return baseService.run(id) ? success() : error("任务不存在或已过期！");
    }

    /**
     * 调度任务批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SCHEDULE_JOB_DEL)")
    @Log(title = "调度任务管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }
}