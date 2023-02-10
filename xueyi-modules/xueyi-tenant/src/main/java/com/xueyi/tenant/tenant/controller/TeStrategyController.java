package com.xueyi.tenant.tenant.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.tenant.domain.query.TeStrategyQuery;
import com.xueyi.tenant.source.service.ITeSourceService;
import com.xueyi.tenant.tenant.service.ITeStrategyService;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 源策略管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/strategy")
public class TeStrategyController extends BaseController<TeStrategyQuery, TeStrategyDto, ITeStrategyService> {

    @Autowired
    private ITeTenantService tenantService;

    @Autowired
    private ITeSourceService sourceService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "数据源策略";
    }

    /**
     * 刷新源策略管理缓存 | 内部调用
     */
    @Override
    @InnerAuth(isAnonymous = true)
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    @GetMapping("/inner/refresh")
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 刷新源策略管理缓存
     */
    @Override
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_EDIT)")
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    @GetMapping("/refresh")
    public AjaxResult refreshCache() {
        return super.refreshCache();
    }

    /**
     * 查询数据源策略列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_LIST)")
    public AjaxResult list(TeStrategyQuery strategy) {
        return super.list(strategy);
    }

    /**
     * 查询源策略详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 数据源策略导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_EXPORT)")
    public void export(HttpServletResponse response, TeStrategyQuery strategy) {
        super.export(response, strategy);
    }

    /**
     * 数据源策略新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_ADD)")
    @Log(title = "源策略管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody TeStrategyDto strategy) {
        return super.add(strategy);
    }

    /**
     * 数据源策略修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_EDIT)")
    @Log(title = "源策略管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody TeStrategyDto strategy) {
        return super.edit(strategy);
    }

    /**
     * 数据源策略修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.TE_STRATEGY_EDIT, @Auth.TE_STRATEGY_ES)")
    @Log(title = "源策略管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody TeStrategyDto strategy) {
        return super.editStatus(strategy);
    }

    /**
     * 数据源策略批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_STRATEGY_DEL)")
    @Log(title = "源策略管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取数据源策略选择框列表
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
    protected void AEHandle(BaseConstants.Operate operate, TeStrategyDto strategy) {
        if (baseService.checkNameUnique(strategy.getId(), strategy.getName()))
            warn(StrUtil.format("{}{}{}失败，{}名称已存在", operate.getInfo(), getNodeName(), strategy.getName(), getNodeName()));
        TeSourceDto source = sourceService.selectById(strategy.getSourceId());
        if (ObjectUtil.isNull(source))
            warn(StrUtil.format("{}{}{}失败，设置的数据源不存在", operate.getInfo(), getNodeName(), strategy.getName()));
        else
            strategy.setSourceSlave(source.getSlave());
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--) {
            if (tenantService.checkStrategyExist(idList.get(i)))
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