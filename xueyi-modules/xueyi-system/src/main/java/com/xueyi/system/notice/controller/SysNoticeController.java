package com.xueyi.system.notice.controller;


import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.NoticeConstants;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.domain.query.SysNoticeQuery;
import com.xueyi.system.notice.service.ISysNoticeService;
import jakarta.servlet.http.HttpServletResponse;
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
 * 系统服务 | 消息模块 | 通知公告管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController<SysNoticeQuery, SysNoticeDto, ISysNoticeService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "通知公告";
    }

    /**
     * 查询通知公告列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_NOTICE_LIST)")
    public AjaxResult list(SysNoticeQuery notice) {
        return super.list(notice);
    }

    /**
     * 查询通知公告详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_NOTICE_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 通知公告导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_NOTICE_EXPORT)")
    @Log(title = "通知公告管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysNoticeQuery notice) {
        super.export(response, notice);
    }

    /**
     * 通知公告新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_NOTICE_ADD)")
    @Log(title = "通知公告管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysNoticeDto notice) {
        return super.add(notice);
    }

    /**
     * 通知公告修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_NOTICE_EDIT)")
    @Log(title = "通知公告管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysNoticeDto notice) {
        return super.edit(notice);
    }

    /**
     * 通知公告修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_NOTICE_EDIT, @Auth.SYS_NOTICE_ES)")
    @Log(title = "通知公告管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysNoticeDto notice) {
        return super.editStatus(notice);
    }

    /**
     * 通知公告批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_NOTICE_DEL)")
    @Log(title = "通知公告管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取通知公告选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysNoticeDto notice) {
        // 初始化发送状态
        if (operate.isAdd())
            notice.setStatus(NoticeConstants.NoticeStatus.READY.getCode());
    }
}
