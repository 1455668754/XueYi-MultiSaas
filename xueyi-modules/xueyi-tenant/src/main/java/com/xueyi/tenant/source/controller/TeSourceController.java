package com.xueyi.tenant.source.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.datasource.utils.DSUtils;
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
    public AjaxResult list(TeSourceDto source) {
        return super.list(source);
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
    public void export(HttpServletResponse response, TeSourceDto source) {
        super.export(response, source);
    }

    /**
     * 数据源连接测试
     */
    @PostMapping("/connection")
    public AjaxResult connection(@Validated @RequestBody TeSourceDto source) {
        DSUtils.testDs(source);
        return success();
    }

    /**
     * 数据源新增
     */
    @Override
    @PostMapping
    @RequiresPermissions("tenant:source:add")
    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody TeSourceDto source) {
        return super.add(source);
    }

    /**
     * 数据源修改
     */
    @Override
    @PutMapping
    @RequiresPermissions("tenant:source:edit")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody TeSourceDto source) {
        return super.edit(source);
    }

    /**
     * 数据源修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {"tenant:source:edit", "tenant:source:editStatus"}, logical = Logical.OR)
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@Validated @RequestBody TeSourceDto source) {
        return super.editStatus(source);
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

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, TeSourceDto source) {
        DSUtils.testDs(source);
        if (baseService.checkNameUnique(source.getId(),  source.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，数据源名称已存在", operate.getInfo(), getNodeName(), source.getName()));
    }
}