package com.xueyi.tenant.tenant.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 租户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TeTenantController extends BaseController<TeTenantDto, ITeTenantService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "租户";
    }

    /**
     * 查询租户列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions("tenant:tenant:list")
    public AjaxResult list(TeTenantDto tenant) {
        return super.list(tenant);
    }

    /**
     * 查询租户详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions("tenant:tenant:single")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 租户导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions("tenant:tenant:export")
    public void export(HttpServletResponse response, TeTenantDto tenant) {
        super.export(response, tenant);
    }

    /**
     * 租户新增
     */
    @Override
    @PostMapping
    @RequiresPermissions("tenant:tenant:add")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody TeTenantDto tenant) {
        return super.add(tenant);
    }

    /**
     * 租户修改
     */
    @Override
    @PutMapping
    @RequiresPermissions("tenant:tenant:edit")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody TeTenantDto tenant) {
        return super.edit(tenant);
    }

    /**
     * 租户修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {"tenant:tenant:edit", "tenant:tenant:editStatus"}, logical = Logical.OR)
    @Log(title = "租户管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@Validated @RequestBody TeTenantDto tenant) {
        return super.editStatus(tenant);
    }

    /**
     * 租户批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions("tenant:tenant:delete")
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void baseRemoveValidated(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--)
            if (baseService.checkIsDefault(idList.get(i)))
                idList.remove(i);
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException(StrUtil.format("删除失败，默认{}不允许删除！", getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            throw new ServiceException(StrUtil.format("默认{}不允许删除，其余{}删除成功！", getNodeName(), getNodeName()));
        }
    }
}