package com.xueyi.system.application.controller;

import com.xueyi.common.core.constant.basic.AppConstants;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;
import com.xueyi.system.application.service.ISysApplicationService;
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
 * 系统服务 | 应用模块 | 应用管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/application")
public class SysApplicationController extends BaseController<SysApplicationQuery, SysApplicationDto, ISysApplicationService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "应用";
    }

    /**
     * 查询消息应用详细
     */
    @TenantIgnore
    @InnerAuth(isAnonymous = true)
    @GetMapping(value = "/inner/{id}/{appType}")
    public R<SysApplicationDto> getInfoByIdInner(@PathVariable("id") Long id, @PathVariable("appType") AppConstants.AppType appType) {
        SysApplicationDto application = baseService.selectById(id);
        if (ObjectUtil.isNull(application) || ObjectUtil.notEqual(application.getType(), appType.getCode()))
            R.fail("应用不存在！");
        return R.ok(application);
    }

    /**
     * 查询消息应用详细
     */
    @TenantIgnore
    @InnerAuth(isAnonymous = true)
    @GetMapping(value = "/inner/app/{appId}/{appType}")
    public R<SysApplicationDto> getInfoByAppIdInner(@PathVariable("appId") String appId, @PathVariable("appType") AppConstants.AppType appType) {
        SysApplicationDto application = baseService.selectByAppId(appId);
        if (ObjectUtil.isNull(application) || ObjectUtil.notEqual(application.getType(), appType.getCode()))
            R.fail("应用不存在！");
        return R.ok(application);
    }

    /**
     * 查询消息应用列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_APPLICATION_LIST)")
    public AjaxResult list(SysApplicationQuery application) {
        return super.list(application);
    }

    /**
     * 查询消息应用详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_APPLICATION_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 消息应用新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_APPLICATION_ADD)")
    @Log(title = "消息应用管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysApplicationDto application) {
        return super.add(application);
    }

    /**
     * 消息应用修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_APPLICATION_EDIT)")
    @Log(title = "消息应用管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysApplicationDto application) {
        return super.edit(application);
    }

    /**
     * 消息应用修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_APPLICATION_EDIT, @Auth.SYS_APPLICATION_ES)")
    @Log(title = "消息应用管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysApplicationDto application) {
        return super.editStatus(application);
    }

    /**
     * 消息应用批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_APPLICATION_DEL)")
    @Log(title = "消息应用管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取消息应用选择框列表
     */
    @GetMapping("/option")
    public AjaxResult option(SysApplicationQuery application) {
        application.setStatus(BaseConstants.Status.NORMAL.getCode());
        return list(application);
    }
}
