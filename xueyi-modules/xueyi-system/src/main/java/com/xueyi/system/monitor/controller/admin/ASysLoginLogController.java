package com.xueyi.system.monitor.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.system.api.log.domain.query.SysLoginLogQuery;
import com.xueyi.system.monitor.controller.base.BSysLoginLogController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统服务 | 监控模块 | 访问日志管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/loginLog")
public class ASysLoginLogController extends BSysLoginLogController {

    /**
     * 查询系统访问记录列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_LOGIN_LOG_LIST)")
    public AjaxResult list(SysLoginLogQuery loginLog) {
        return super.list(loginLog);
    }

    /**
     * 系统访问记录导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_LOGIN_LOG_EXPORT)")
    @Log(title = "访问日志", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysLoginLogQuery loginLog) {
        super.export(response, loginLog);
    }

    /**
     * 系统访问记录批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_LOGIN_LOG_DEL)")
    @Log(title = "访问日志", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 系统访问记录清空
     */
    @DeleteMapping("/clean")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_LOGIN_LOG_DEL)")
    @Log(title = "访问日志", businessType = BusinessType.CLEAN)
    public AjaxResult clean() {
        baseService.cleanLoginLog();
        return success();
    }
}
