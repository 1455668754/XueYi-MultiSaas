package com.xueyi.gen.controller;

import com.alibaba.fastjson.JSONObject;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.text.Convert;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.web.entity.controller.SubBaseController;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.service.IGenTableColumnService;
import com.xueyi.gen.service.IGenTableService;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/gen")
public class GenController extends SubBaseController<GenTableDto, IGenTableService, GenTableColumnDto, IGenTableColumnService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "业务表";
    }

    /** 定义子数据名称 */
    protected String getSubName() {
        return "业务字段";
    }

    /**
     * 查询数据库列表
     */
//    @RequiresPermissions("tool:gen:list")
    @GetMapping("/db/list")
    public AjaxResult dataList(GenTableDto table) {
        startPage();
        List<GenTableDto> list = baseService.selectDbTableList(table);
        return getDataTable(list);
    }

    /**
     * 查询数据表字段列表
     */
    @GetMapping(value = "/column/{tableId}")
    public AjaxResult columnList(@PathVariable Long tableId) {
        startPage();
        List<GenTableColumnDto> list = baseService.selectSubByForeignKey(tableId);
        return getDataTable(list);
    }

    /**
     * 导入表结构（保存）
     */
//    @RequiresPermissions("tool:gen:import")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTableDto> tableList = baseService.selectDbTableListByNames(tableNames);
        baseService.importGenTable(tableList);
        return AjaxResult.success();
    }

    /**
     * 预览代码
     */
//    @RequiresPermissions("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) {
        List<JSONObject> dataMap = baseService.previewCode(tableId);
        return AjaxResult.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
//    @RequiresPermissions("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableId}")
    public void download(HttpServletResponse response, @PathVariable("tableId") Long tableId) throws IOException {
        byte[] data = baseService.downloadCode(tableId);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
//    @RequiresPermissions("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableId}")
    public AjaxResult genCode(@PathVariable("tableId") Long tableId) {
        baseService.generatorCode(tableId);
        return AjaxResult.success();
    }

    /**
     * 同步数据库
     */
//    @RequiresPermissions("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/syncDb/{tableName}")
    public AjaxResult syncDb(@PathVariable("tableName") String tableName) {
        baseService.syncDb(tableName);
        return AjaxResult.success();
    }

    /**
     * 批量生成代码
     */
//    @RequiresPermissions("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, Long[] ids) throws IOException {
        byte[] data = baseService.downloadCode(ids);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, GenTableDto table) {
        if (operate.isEdit())
            baseService.validateEdit(table);
    }
}
