package com.xueyi.gen.controller.admin;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.gen.controller.base.BGenController;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableColumnQuery;
import com.xueyi.gen.domain.query.GenTableQuery;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 代码生成管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/gen")
public class AGenController extends BGenController {

    /**
     * 查询代码生成列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_LIST)")
    public AjaxResult list(GenTableQuery table) {
        return super.list(table);
    }

    /**
     * 查询数据库列表
     */
    @GetMapping("/db/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_LIST)")
    public AjaxResult dataList(GenTableQuery table) {
        startPage();
        List<GenTableDto> list = baseService.selectDbTableList(table);
        return getDataTable(list);
    }

    /**
     * 查询数据表字段列表
     */
    @GetMapping(value = "/column/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_LIST)")
    public AjaxResult columnList(GenTableColumnQuery query) {
        startPage();
        List<GenTableColumnDto> list = subService.selectList(query);
        return getDataTable(list);
    }

    /**
     * 查询代码生成详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询代码生成详细 | 包含代码生成数据
     */
    @GetMapping(value = "/sub/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_SINGLE)")
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_PREVIEW)")
    public AjaxResult previewMulti(@PathVariable("tableId") Long tableId) {
        List<JSONObject> dataMap = baseService.previewCode(tableId);
        return success(dataMap);
    }

    /**
     * 导入表结构（保存）
     */
    @PostMapping("/importTable")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_IMPORT)")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    public AjaxResult importTableSave(@RequestParam("tables") String tables, @RequestParam(value = "sourceName", required = false) String sourceName) {
        String[] tableNames = ConvertUtil.toStrArray(tables);
        // 查询表信息
        List<GenTableDto> tableList = baseService.selectDbTableListByNames(tableNames, sourceName);
        baseService.importGenTable(tableList);
        return success();
    }

    /**
     * 代码生成修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_EDIT)")
    @Log(title = "代码生成管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody GenTableDto table) {
        return super.edit(table);
    }

    /**
     * 生成代码（下载方式）
     */
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_CODE)")
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/download/{tableId}")
    public void downloadMulti(HttpServletResponse response, @PathVariable("tableId") Long tableId) throws IOException {
        byte[] data = baseService.downloadCode(tableId);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_CODE)")
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/generate/{tableId}")
    public AjaxResult genMultiCode(@PathVariable("tableId") Long tableId) {
        baseService.generatorCode(tableId);
        return success();
    }

    /**
     * 批量生成代码
     */
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_CODE)")
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/batchGenCode")
    public void batchMultiGenCode(HttpServletResponse response, Long[] ids) throws IOException {
        byte[] data = baseService.downloadCode(ids);
        genCode(response, data);
    }

    /**
     * 代码生成强制批量删除
     */
    @Override
    @DeleteMapping("/batch/force/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.GEN_GENERATE_DEL)")
    @Log(title = "代码生成管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        return super.batchRemoveForce(idList);
    }

}
