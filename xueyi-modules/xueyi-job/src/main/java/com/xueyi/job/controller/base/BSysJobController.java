package com.xueyi.job.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.query.SysJobQuery;
import com.xueyi.job.api.utils.CronUtils;
import com.xueyi.job.constant.ScheduleConstants;
import com.xueyi.job.service.ISysJobService;
import com.xueyi.job.util.ScheduleUtil;

/**
 * 定时任务 | 调度任务管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysJobController extends BaseController<SysJobQuery, SysJobDto, ISysJobService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "调度任务";
    }

    /**
     * 前置校验 增加/修改
     */
    protected void AEHandle(BaseConstants.Operate operate, SysJobDto job) {
        if (!CronUtils.isValid(job.getCronExpression())) {
            warn(StrUtil.format("{}{}{}失败，Cron表达式不正确", operate.getInfo(), getNodeName(), job.getName()));
        } else if (StrUtil.containsIgnoreCase(job.getInvokeTarget(), HttpConstants.Type.LOOKUP_RMI.getCode())) {
            warn(StrUtil.format("{}{}{}失败，目标字符串不允许'rmi'调用", operate.getInfo(), getNodeName(), job.getName()));
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{HttpConstants.Type.LOOKUP_LDAP.getCode(), HttpConstants.Type.LOOKUP_LDAPS.getCode()})) {
            warn(StrUtil.format("{}{}{}失败，目标字符串不允许'ldap(s)'调用", operate.getInfo(), getNodeName(), job.getName()));
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{HttpConstants.Type.HTTP.getCode(), HttpConstants.Type.HTTPS.getCode()})) {
            warn(StrUtil.format("{}{}{}失败，目标字符串不允许'http(s)'调用", operate.getInfo(), getNodeName(), job.getName()));
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), ScheduleConstants.JOB_ERROR_STR)) {
            warn(StrUtil.format("{}{}{}失败，目标字符串存在违规", operate.getInfo(), getNodeName(), job.getName()));
        } else if (!ScheduleUtil.whiteList(job.getInvokeTarget())) {
            warn(StrUtil.format("{}{}{}失败，目标字符串不在白名单内", operate.getInfo(), getNodeName(), job.getName()));
        }
    }
}