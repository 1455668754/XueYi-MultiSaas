package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import com.xueyi.system.monitor.service.ISysOperateLogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 系统服务 | 监控模块 | 操作日志管理 业务处理
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
    @PostMapping
    @InnerAuth(isAnonymous = true)
    public R<Boolean> addInner(@RequestBody SysOperateLogDto operateLog) {
        baseService.insert(operateLog);
        return R.ok();
    }

    /**
     * 查询操作日志列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_OPERATE_LOG_LIST)")
    public AjaxResult list(SysOperateLogQuery operateLog) {
        return super.list(operateLog);
    }

    /**
     * 查询操作日志详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_OPERATE_LOG_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 操作日志导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_OPERATE_LOG_EXPORT)")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysOperateLogQuery operateLog) {
        super.export(response, operateLog);
    }

    /**
     * 操作日志批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_OPERATE_LOG_DEL)")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 操作日志清空
     */
    @DeleteMapping("/clean")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_OPERATE_LOG_DEL)")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    public AjaxResult clean() {
        baseService.cleanOperateLog();
        return success();
    }
}
