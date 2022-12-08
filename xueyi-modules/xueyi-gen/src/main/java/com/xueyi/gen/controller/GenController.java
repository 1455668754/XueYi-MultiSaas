package com.xueyi.gen.controller;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.service.IGenTableService;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 代码生成管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/gen")
public class GenController extends BaseController<GenTableQuery, GenTableDto, IGenTableService> {

    /**
     * 定义节点名称
     */
    @Override
    protected String getNodeName() {
        return "业务表" ;
    }

    /**
     * 查询代码生成列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.GENERATE_GEN_LIST)
    public AjaxResult list(GenTableQuery table) {
        return super.list(table);
    }

    /**
     * 查询数据库列表
     */
    @GetMapping("/db/list")
    @RequiresPermissions(Auth.GENERATE_GEN_LIST)
    public AjaxResult dataList(GenTableDto table) {
        startPage();
        List<GenTableDto> list = baseService.selectDbTableList(table);
        return getDataTable(list);
    }

    /**
     * 查询代码生成详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.GENERATE_GEN_SINGLE)
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询代码生成详细 | 包含代码生成数据
     */
    @GetMapping(value = "/sub/{id}")
    @RequiresPermissions(Auth.GENERATE_GEN_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 预览代码
     */
    @GetMapping("/cloud/preview/{tableId}")
    @RequiresPermissions(Auth.GENERATE_GEN_PREVIEW)
    public AjaxResult previewCloud(@PathVariable("tableId") Long tableId) {
        List<JSONObject> dataMap = baseService.previewCode(tableId, ServiceConstants.FromSource.CLOUD);
        return success(dataMap);
    }

    /**
     * 预览代码
     */
    @GetMapping("/multi/preview/{tableId}")
    @RequiresPermissions(Auth.GENERATE_GEN_PREVIEW)
    public AjaxResult previewMulti(@PathVariable("tableId") Long tableId) {
        List<JSONObject> dataMap = baseService.previewCode(tableId, ServiceConstants.FromSource.MULTI);
        return success(dataMap);
    }

    /**
     * 查询数据表字段列表
     */
    @GetMapping(value = "/column/{tableId}")
    @RequiresPermissions(Auth.GENERATE_GEN_LIST)
    public AjaxResult columnList(@PathVariable Long tableId) {
        throw new ServiceException("此处为错误，待调整！！！！");
//        startPage();
//        List<GenTableColumnDto> list = baseService.selectList(tableId);
//        return getDataTable(list);
    }

    /**
     * 导入表结构（保存）
     */
    @PostMapping("/importTable")
    @RequiresPermissions(Auth.GENERATE_GEN_IMPORT)
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    public AjaxResult importTableSave(@RequestParam("tables") String tables) {
        String[] tableNames = ConvertUtil.toStrArray(tables);
        // 查询表信息
        List<GenTableDto> tableList = baseService.selectDbTableListByNames(tableNames);
        baseService.importGenTable(tableList);
        return success();
    }

    /**
     * 代码生成修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.GENERATE_GEN_EDIT)
    @Log(title = "代码生成管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody GenTableDto table) {
        return super.edit(table);
    }

    /**
     * 生成代码（下载方式）
     */
    @RequiresPermissions(Auth.GENERATE_GEN_CODE)
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/cloud/download/{tableId}")
    public void downloadCloud(HttpServletResponse response, @PathVariable("tableId") Long tableId) throws IOException {
        byte[] data = baseService.downloadCode(tableId, ServiceConstants.FromSource.CLOUD);
        genCode(response, data);
    }

    /**
     * 生成代码（下载方式）
     */
    @RequiresPermissions(Auth.GENERATE_GEN_CODE)
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/multi/download/{tableId}")
    public void downloadMulti(HttpServletResponse response, @PathVariable("tableId") Long tableId) throws IOException {
        byte[] data = baseService.downloadCode(tableId, ServiceConstants.FromSource.MULTI);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @RequiresPermissions(Auth.GENERATE_GEN_CODE)
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/cloud/generate/{tableId}")
    public AjaxResult genCloudCode(@PathVariable("tableId") Long tableId) {
        baseService.generatorCode(tableId, ServiceConstants.FromSource.CLOUD);
        return success();
    }

    /**
     * 生成代码（自定义路径）
     */
    @RequiresPermissions(Auth.GENERATE_GEN_CODE)
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/multi/generate/{tableId}")
    public AjaxResult genMultiCode(@PathVariable("tableId") Long tableId) {
        baseService.generatorCode(tableId, ServiceConstants.FromSource.MULTI);
        return success();
    }

    /**
     * 批量生成代码
     */
    @RequiresPermissions(Auth.GENERATE_GEN_CODE)
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/cloud/batchGenCode")
    public void batchCloudGenCode(HttpServletResponse response, Long[] ids) throws IOException {
        byte[] data = baseService.downloadCode(ids, ServiceConstants.FromSource.CLOUD);
        genCode(response, data);
    }

    /**
     * 批量生成代码
     */
    @RequiresPermissions(Auth.GENERATE_GEN_CODE)
    @Log(title = "代码生成", businessType = BusinessType.GEN_CODE)
    @GetMapping("/multi/batchGenCode")
    public void batchMultiGenCode(HttpServletResponse response, Long[] ids) throws IOException {
        byte[] data = baseService.downloadCode(ids, ServiceConstants.FromSource.MULTI);
        genCode(response, data);
    }

    /**
     * 代码生成强制批量删除
     */
    @Override
    @DeleteMapping("/batch/force/{idList}")
    @RequiresPermissions(Auth.GENERATE_GEN_DEL)
    @Log(title = "代码生成管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        return super.batchRemoveForce(idList);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"xueyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, GenTableDto table) {
        if (operate.isEdit())
            baseService.validateEdit(table);
    }
}
