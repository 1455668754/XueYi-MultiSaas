package com.xueyi.tenant.source.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.datasource.utils.DSUtil;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.controller.base.BTeSourceController;
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
 * 数据源管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class ATeSourceController extends BTeSourceController {

    /**
     * 刷新数据源缓存
     */
    @Override
    @GetMapping("/refresh")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_EDIT)")
    @Log(title = "数据源管理", businessType = BusinessType.REFRESH)
    public AjaxResult refreshCache() {
        return super.refreshCache();
    }

    /**
     * 查询数据源列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_LIST)")
    public AjaxResult list(TeSourceQuery source) {
        return super.list(source);
    }

    /**
     * 查询数据源详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 数据源导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_EXPORT)")
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
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_ADD)")
    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody TeSourceDto source) {
        return super.add(source);
    }

    /**
     * 数据源修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_EDIT)")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody TeSourceDto source) {
        return super.edit(source);
    }

    /**
     * 数据源修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.TE_SOURCE_EDIT, @Auth.TE_SOURCE_ES)")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody TeSourceDto source) {
        return super.editStatus(source);
    }

    /**
     * 数据源批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.TE_SOURCE_DEL)")
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

}