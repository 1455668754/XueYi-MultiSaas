package com.xueyi.tenant.source.controller;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.source.service.ITeSourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 数据源管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class TeSourceController extends BaseController<TeSourceDto, ITeSourceService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "数据源";
    }

    /**
     * 查询数据源列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions("tenant:source:list")
    public AjaxResult list(TeSourceDto teSource) {
        return super.list(teSource);
    }

    /**
     * 查询数据源详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions("tenant:source:single")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 数据源导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions("tenant:source:export")
    public void export(HttpServletResponse response, TeSourceDto teSource) {
        super.export(response, teSource);
    }

    /**
     * 数据源新增
     */
    @Override
    @PostMapping
    @RequiresPermissions("tenant:source:add")
    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody TeSourceDto teSource) {
        return super.add(teSource);
    }

    /**
     * 数据源修改
     */
    @Override
    @PutMapping
    @RequiresPermissions("tenant:source:edit")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody TeSourceDto teSource) {
        return super.edit(teSource);
    }

    /**
     * 数据源修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {"tenant:source:edit", "tenant:source:editStatus"}, logical = Logical.OR)
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@Validated @RequestBody TeSourceDto teSource) {
        return super.editStatus(teSource);
    }

    /**
     * 数据源批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions("tenant:source:delete")
    @Log(title = "数据源管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }
}