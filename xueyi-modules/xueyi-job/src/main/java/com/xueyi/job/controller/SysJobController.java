package com.xueyi.job.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.job.ScheduleConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.query.SysJobQuery;
import com.xueyi.job.api.utils.CronUtils;
import com.xueyi.job.service.ISysJobService;
import com.xueyi.job.util.ScheduleUtils;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 调度任务管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/job")
public class SysJobController extends BaseController<SysJobQuery, SysJobDto, ISysJobService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "调度任务";
    }

    /**
     * 查询定时任务列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SCHEDULE_JOB_LIST)
    public AjaxResult list(SysJobQuery job) {
        return super.list(job);
    }

    /**
     * 查询调度任务详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SCHEDULE_JOB_SINGLE)
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 调度任务导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SCHEDULE_JOB_EXPORT)
    public void export(HttpServletResponse response, SysJobQuery job) {
        super.export(response, job);
    }

    /**
     * 调度任务新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SCHEDULE_JOB_ADD)
    @Log(title = "调度任务管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysJobDto job) {
        return super.add(job);
    }

    /**
     * 调度任务修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SCHEDULE_JOB_EDIT)
    @Log(title = "调度任务管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysJobDto job) {
        return super.edit(job);
    }

    /**
     * 调度任务修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SCHEDULE_JOB_EDIT, Auth.SCHEDULE_JOB_ES}, logical = Logical.OR)
    @Log(title = "调度任务管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysJobDto job) {
        return super.editStatus(job);
    }

    /**
     * 定时任务立即执行一次
     */
    @GetMapping("/run/{id}")
    @RequiresPermissions(Auth.SCHEDULE_JOB_EDIT)
    @Log(title = "定时任务 - 执行一次", businessType = BusinessType.UPDATE)
    public AjaxResult run(@PathVariable Long id) throws SchedulerException {
        return baseService.run(id) ? success() : error("任务不存在或已过期！");
    }

    /**
     * 调度任务批量删除
     */
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SCHEDULE_JOB_DEL)
    @Log(title = "调度任务管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        if (CollUtil.isEmpty(idList))
            warn(StrUtil.format("无待删除{}！", getNodeName()));
        return toAjax(baseService.deleteByIds(idList));
    }

    /**
     * 获取调度任务选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    protected void AEHandle(BaseConstants.Operate operate, SysJobDto job) {
        if (!CronUtils.isValid(job.getCronExpression()))
            warn(StrUtil.format("{}{}{}失败，Cron表达式不正确", operate.getInfo(), getNodeName(), job.getName()));
        else if (StrUtil.containsIgnoreCase(job.getInvokeTarget(), HttpConstants.Type.LOOKUP_RMI.getCode()))
            warn(StrUtil.format("{}{}{}失败，目标字符串不允许'rmi'调用", operate.getInfo(), getNodeName(), job.getName()));
        else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{HttpConstants.Type.LOOKUP_LDAP.getCode(), HttpConstants.Type.LOOKUP_LDAPS.getCode()}))
            warn(StrUtil.format("{}{}{}失败，目标字符串不允许'ldap(s)'调用", operate.getInfo(), getNodeName(), job.getName()));
        else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{HttpConstants.Type.HTTP.getCode(), HttpConstants.Type.HTTPS.getCode()}))
            warn(StrUtil.format("{}{}{}失败，目标字符串不允许'http(s)'调用", operate.getInfo(), getNodeName(), job.getName()));
        else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), ScheduleConstants.JOB_ERROR_STR))
            warn(StrUtil.format("{}{}{}失败，目标字符串存在违规", operate.getInfo(), getNodeName(), job.getName()));
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
            warn(StrUtil.format("{}{}{}失败，目标字符串不在白名单内", operate.getInfo(), getNodeName(), job.getName()));
    }
}