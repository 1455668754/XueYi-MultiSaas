package com.xueyi.tenant.source.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.source.domain.query.TeStrategyQuery;
import com.xueyi.tenant.source.controller.base.BTeStrategyController;
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
 * 源策略管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/strategy")
public class ATeStrategyController extends BTeStrategyController {

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

}