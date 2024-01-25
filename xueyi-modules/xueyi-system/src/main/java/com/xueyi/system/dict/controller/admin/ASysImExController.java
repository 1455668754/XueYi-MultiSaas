package com.xueyi.system.dict.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.domain.query.SysImExQuery;
import com.xueyi.system.dict.controller.base.BSysImExController;
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
 * 导入导出配置管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/imExConfig")
public class ASysImExController extends BSysImExController {

    /**
     * 查询导入导出配置列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_IMPORT_EXPORT_LIST)")
    public AjaxResult list(SysImExQuery importExport) {
        return super.list(importExport);
    }

    /**
     * 查询导入导出配置详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_IMPORT_EXPORT_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 导入导出配置新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_IMPORT_EXPORT_ADD)")
    @Log(title = "导入导出配置管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysImExDto importExport) {
        return super.add(importExport);
    }

    /**
     * 导入导出配置修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_IMPORT_EXPORT_EDIT)")
    @Log(title = "导入导出配置管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysImExDto importExport) {
        return super.edit(importExport);
    }

    /**
     * 导入导出配置批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_IMPORT_EXPORT_DEL)")
    @Log(title = "导入导出配置管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    interface Auth {
        /** 导入导出配置管理 - 列表 */
        String SYS_IMPORT_EXPORT_LIST = "system:export:list";
        /** 导入导出配置管理 - 详情 */
        String SYS_IMPORT_EXPORT_SINGLE = "system:export:single";
        /** 导入导出配置管理 - 新增 */
        String SYS_IMPORT_EXPORT_ADD = "system:export:add";
        /** 导入导出配置管理 - 修改 */
        String SYS_IMPORT_EXPORT_EDIT = "system:export:edit";
        /** 导入导出配置管理 - 删除 */
        String SYS_IMPORT_EXPORT_DEL = "system:export:delete";
    }
}
