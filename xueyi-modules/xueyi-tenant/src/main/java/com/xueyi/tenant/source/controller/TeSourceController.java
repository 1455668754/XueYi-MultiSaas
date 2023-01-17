package com.xueyi.tenant.source.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.datasource.utils.DSUtil;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.service.ITeSourceService;
import com.xueyi.tenant.tenant.service.ITeStrategyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 数据源管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class TeSourceController extends BaseController<TeSourceQuery, TeSourceDto, ITeSourceService> {

    @Autowired
    private ITeStrategyService strategyService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "数据源";
    }

    /**
     * 刷新数据源缓存 | 内部调用
     */
    @Override
    @InnerAuth
    @GetMapping("/inner/refresh")
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 刷新数据源缓存
     */
    @Override
    @RequiresPermissions(Auth.TE_SOURCE_EDIT)
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    @GetMapping("/refresh")
    public AjaxResult refreshCache() {
        return super.refreshCache();
    }

    /**
     * 查询数据源列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.TE_SOURCE_LIST)
    public AjaxResult list(TeSourceQuery source) {
        return super.list(source);
    }

    /**
     * 查询数据源详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.TE_SOURCE_SINGLE)
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 数据源导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.TE_SOURCE_EXPORT)
    public void export(HttpServletResponse response, TeSourceQuery source) {
        super.export(response, source);
    }

    /**
     * 数据源连接测试
     */
    @PostMapping("/connection")
    public AjaxResult connection(@Validated @RequestBody TeSourceDto source) {
        DSUtil.testSlaveDs(source);
        return success();
    }

    /**
     * 数据源新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.TE_SOURCE_ADD)
    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody TeSourceDto source) {
        return super.add(source);
    }

    /**
     * 数据源修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.TE_SOURCE_EDIT)
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody TeSourceDto source) {
        return super.edit(source);
    }

    /**
     * 数据源修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.TE_SOURCE_EDIT, Auth.TE_SOURCE_ES}, logical = Logical.OR)
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody TeSourceDto source) {
        return super.editStatus(source);
    }

    /**
     * 数据源批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.TE_SOURCE_DEL)
    @Log(title = "数据源管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取数据源选择框列表
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
    protected void AEHandle(BaseConstants.Operate operate, TeSourceDto source) {
        DSUtil.testSlaveDs(source);
        if (baseService.checkNameUnique(source.getId(), source.getName()))
            warn(StrUtil.format("{}{}{}失败，{}名称已存在", operate.getInfo(), getNodeName(), source.getName(), getNodeName()));
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--) {
            if (strategyService.checkSourceExist(idList.get(i)))
                idList.remove(i);
            else if (baseService.checkIsDefault(idList.get(i)))
                idList.remove(i);
        }
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("删除失败，默认{}及已被使用的{}不允许删除！", getNodeName(), getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            warn(StrUtil.format("默认{}及已被使用的{}不允许删除，其余{}删除成功！", getNodeName(), getNodeName(), getNodeName()));
        }
    }
}