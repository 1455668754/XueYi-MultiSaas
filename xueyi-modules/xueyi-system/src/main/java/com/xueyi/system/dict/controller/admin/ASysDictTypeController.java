package com.xueyi.system.dict.controller.admin;

import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.controller.base.BSysDictTypeController;
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
 * 系统服务 | 字典模块 | 字典类型管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/dict/type")
public class ASysDictTypeController extends BSysDictTypeController {

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    @GetMapping(value = "/type/{code}")
    public AjaxResult listByCode(@PathVariable String code) {
        return super.listByCode(code);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    @GetMapping(value = "/types/{codeList}")
    public AjaxResult listByCodeList(@PathVariable List<String> codeList) {
        return super.listByCodeList(codeList);
    }

    /**
     * 查询字典类型列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_LIST)")
    public AjaxResult list(SysDictTypeQuery query) {
        if (SecurityUserUtils.isAdminTenant()) {
            SecurityContextHolder.setTenantIgnore();
        }
        return super.list(query);
    }

    /**
     * 查询字典类型详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        if (SecurityUserUtils.isAdminTenant()) {
            SecurityContextHolder.setTenantIgnore();
        }
        return super.getInfo(id);
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
        if (SecurityUserUtils.isAdminTenant()) {
            SecurityContextHolder.setTenantIgnore();
        }
        return super.batchRemove(idList);
    }

    /**
     * 刷新字典缓存
     */
    @Override
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_EDIT)")
    @Log(title = "字典类型", businessType = BusinessType.REFRESH)
    @GetMapping("/refresh")
    public AjaxResult refreshCache() {
        baseService.clearCache();
        return AjaxResult.success();
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
