package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.monitor.service.ISysOperateLogService;
import org.springframework.web.bind.annotation.*;

;
/**
 * 操作日志管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/operationLog")
public class SysOperateLogController extends BaseController<SysOperateLogDto, ISysOperateLogService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "操作日志";
    }

    @RequiresPermissions("system:operationLog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        baseService.cleanOperationLog();
        return AjaxResult.success();
    }

    @Override
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysOperateLogDto operationLog) {
        return super.add(operationLog);
    }
}
