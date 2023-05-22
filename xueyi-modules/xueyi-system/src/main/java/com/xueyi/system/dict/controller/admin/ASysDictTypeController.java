package com.xueyi.system.dict.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.controller.base.BSysDictTypeController;
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
 * 字典类型管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dict/type")
public class ASysDictTypeController extends BSysDictTypeController {

    /**
     * 查询字典类型列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_LIST)")
    public AjaxResult list(SysDictTypeQuery dictType) {
        return super.list(dictType);
    }

    /**
     * 查询字典类型详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 字典类型导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_EXPORT)")
    @Log(title = "字典类型管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysDictTypeQuery dictType) {
        super.export(response, dictType);
    }

    /**
     * 字典类型新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_ADD)")
    @Log(title = "字典类型管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysDictTypeDto dictType) {
        return super.add(dictType);
    }

    /**
     * 字典类型修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_EDIT)")
    @Log(title = "字典类型管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysDictTypeDto dictType) {
        return super.edit(dictType);
    }

    /**
     * 字典类型修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_DICT_EDIT, @Auth.SYS_DICT_ES)")
    @Log(title = "字典类型管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysDictTypeDto dictType) {
        return super.editStatus(dictType);
    }

    /**
     * 字典类型批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_DEL)")
    @Log(title = "字典类型管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 字典类型强制批量删除
     */
    @Override
    @DeleteMapping("/batch/force/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_DEL)")
    @Log(title = "字典类型管理", businessType = BusinessType.DELETE_FORCE)
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        return super.batchRemoveForce(idList);
    }

    /**
     * 刷新字典缓存
     */
    @Override
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_EDIT)")
    @Log(title = "字典类型", businessType = BusinessType.REFRESH)
    @GetMapping("/refresh")
    public AjaxResult refreshCache() {
        return super.refreshCache();
    }

    /**
     * 获取字典类型选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

}
