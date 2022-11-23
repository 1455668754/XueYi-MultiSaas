package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import com.xueyi.system.monitor.service.ISysOperateLogService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 操作日志管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/operateLog")
public class SysOperateLogController extends BaseController<SysOperateLogQuery, SysOperateLogDto, ISysOperateLogService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "操作日志";
    }

    /**
     * 新增操作日志 | 内部调用
     */
    @InnerAuth
    @PostMapping
    public R<Boolean> addInner(@RequestBody SysOperateLogDto operateLog) {
        baseService.insert(operateLog);
        return R.ok();
    }

    /**
     * 查询操作日志列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_OPERATE_LOG_LIST)
    public AjaxResult list(SysOperateLogQuery operateLog) {
        return super.list(operateLog);
    }

    /**
     * 查询操作日志详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_OPERATE_LOG_SINGLE)
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 操作日志导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_OPERATE_LOG_EXPORT)
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysOperateLogQuery operateLog) {
        super.export(response, operateLog);
    }

    /**
     * 操作日志批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_OPERATE_LOG_DEL)
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 操作日志清空
     */
    @DeleteMapping("/clean")
    @RequiresPermissions(Auth.SYS_OPERATE_LOG_DEL)
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    public AjaxResult clean() {
        baseService.cleanOperateLog();
        return success();
    }
}
