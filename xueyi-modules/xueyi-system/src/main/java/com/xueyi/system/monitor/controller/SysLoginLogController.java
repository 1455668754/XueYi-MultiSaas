package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.monitor.service.ISysLoginLogService;
import org.springframework.web.bind.annotation.*;

;
/**
 * 访问日志管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/loginLog")
public class SysLoginLogController extends BaseController<SysLoginLogDto, ISysLoginLogService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "访问日志";
    }

    @RequiresPermissions("system:loginLog:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        baseService.cleanLoginLog();
        return AjaxResult.success();
    }

    @Override
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLoginLogDto loginInfo) {
        return super.add(loginInfo);
    }
}
