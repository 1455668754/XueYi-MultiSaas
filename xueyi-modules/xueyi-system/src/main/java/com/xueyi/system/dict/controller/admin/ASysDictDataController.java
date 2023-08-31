package com.xueyi.system.dict.controller.admin;

import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.dict.controller.base.BSysDictDataController;
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
 * 系统服务 | 字典模块 | 字典数据管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/dict/data")
public class ASysDictDataController extends BSysDictDataController {

    /**
     * 查询字典数据列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_LIST)")
    public AjaxResult list(SysDictDataQuery query) {
        if (SecurityUserUtils.isAdminTenant()) {
            SecurityContextHolder.setTenantIgnore();
        }
        return super.list(query);
    }

    /**
     * 查询字典数据详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_LIST)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        if (SecurityUserUtils.isAdminTenant()) {
            SecurityContextHolder.setTenantIgnore();
        }
        return super.getInfo(id);
    }

    /**
     * 字典数据新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_DICT)")
    @Log(title = "字典数据管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysDictDataDto dictData) {
        return super.add(dictData);
    }

    /**
     * 字典数据修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_DICT)")
    @Log(title = "字典数据管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysDictDataDto dictData) {
        return super.edit(dictData);
    }

    /**
     * 字典数据修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_DICT)")
    @Log(title = "字典数据管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysDictDataDto dictData) {
        return super.editStatus(dictData);
    }

    /**
     * 字典数据批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_DICT_DICT)")
    @Log(title = "字典数据管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }
}
