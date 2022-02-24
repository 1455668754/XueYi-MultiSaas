package com.xueyi.system.notice.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.service.ISysNoticeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 通知公告管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController<SysNoticeDto, ISysNoticeService> {

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
    @RequiresPermissions("system:notice:list")
    public AjaxResult listExtra(SysNoticeDto notice) {
        return super.listExtra(notice);
    }

    /**
     * 查询通知公告详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions("system:notice:single")
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 通知公告导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions("system:notice:export")
    public void export(HttpServletResponse response, SysNoticeDto notice) {
        super.export(response, notice);
    }

    /**
     * 通知公告新增
     */
    @Override
    @PostMapping
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysNoticeDto notice) {
        return super.add(notice);
    }

    /**
     * 通知公告修改
     */
    @Override
    @PutMapping
    @RequiresPermissions("system:notice:edit")
    @Log(title = "通知公告管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysNoticeDto notice) {
        return super.edit(notice);
    }

    /**
     * 通知公告修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {"system:notice:edit", "system:notice:editStatus"}, logical = Logical.OR)
    @Log(title = "通知公告管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysNoticeDto notice) {
        return super.editStatus(notice);
    }

    /**
     * 通知公告批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions("system:notice:delete")
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
}
