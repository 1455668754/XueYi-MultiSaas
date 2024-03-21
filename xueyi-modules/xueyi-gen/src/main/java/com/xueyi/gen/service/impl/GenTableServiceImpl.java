package com.xueyi.gen.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CharsetUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.constant.GenConstants.OptionField;
import com.xueyi.gen.constant.GenConstants.TemplateType;
import com.xueyi.gen.domain.correlate.GenTableCorrelate;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.manager.IGenTableManager;
import com.xueyi.gen.service.IGenTableColumnService;
import com.xueyi.gen.service.IGenTableService;
import com.xueyi.gen.util.GenUtils;
import com.xueyi.gen.util.VelocityInitializer;
import com.xueyi.gen.util.VelocityUtils;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.feign.RemoteMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.xueyi.system.api.authority.constant.AuthorityConstants.MENU_TOP_NODE;

/**
 * 业务管理 业务层处理
 *
 * @author xueyi
 */
@Slf4j
@Service
public class GenTableServiceImpl extends BaseServiceImpl<GenTableQuery, GenTableDto, GenTableCorrelate, IGenTableManager> implements IGenTableService {

    @Autowired
    private RemoteMenuService remoteMenuService;

    @Autowired
    private IGenTableColumnService subService;

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, GenTableCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.SELECT, GenTableCorrelate.INFO_LIST);
            put(CorrelateConstants.ServiceType.DELETE, GenTableCorrelate.BASE_DEL);
        }};
    }

    /**
     * 查询数据库列表
     *
     * @param table 业务对象
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableList(GenTableQuery table) {
        return SecurityContextHolder.setSourceNameFun(StrUtil.isNotBlank(table.getSourceName()) ? table.getSourceName() : TenantConstants.Source.MASTER.getCode(), () -> baseManager.selectDbTableList(table));
    }

    /**
     * 根据表名称组查询数据库列表
     *
     * @param tableNames 表名称组
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] tableNames, String sourceName) {
        return SecurityContextHolder.setSourceNameFun(StrUtil.isNotBlank(sourceName) ? sourceName : TenantConstants.Source.MASTER.getCode(), () -> baseManager.selectDbTableListByNames(tableNames));
    }

    /**
     * 导入表结构
     *
     * @param tableList  导入表列表
     * @param sourceName 数据源
     */

    @Override
    @DSTransactional
    public void importGenTable(List<GenTableDto> tableList, String sourceName) {
        try {
            tableList.forEach(table -> {
                GenUtils.initTable(table);
                int row = baseManager.insert(table);
                if (row > 0) {
                    List<GenTableColumnDto> columnList = subService.selectDbTableColumnsByName(table.getName(), sourceName);
                    columnList.forEach(column -> GenUtils.initColumnField(column, table));
                    subService.insertBatch(columnList);
                    GenUtils.initTableOptions(columnList, table);
                    baseManager.update(table);
                }
            });
        } catch (Exception e) {
            AjaxResult.warn(StrUtil.format("导入失败：{}", e.getMessage()));
        }
    }

    /**
     * 修改业务
     *
     * @param table 业务信息
     */
    @Override
    @DSTransactional
    public int update(GenTableDto table) {
        int row = baseManager.update(table);
        if (row > 0) {
            GenUtils.updateCheckColumn(table);
        }
        table.getSubList().forEach(column -> subService.update(column));
        return row;
    }

    /**
     * 预览代码
     *
     * @param id Id
     * @return 预览数据列表
     */
    @Override
    public List<JSONObject> previewCode(Long id) {
        List<JSONObject> dataMap = new ArrayList<>();
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        JSONObject data;
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            data = new JSONObject();
            String vmName = StrUtil.subAfter(template, StrUtil.SLASH, true);
            vmName = StrUtil.removeSuffix(vmName, (".vm"));
            data.put("name", vmName);
            data.put("language", StrUtil.subAfter(vmName, StrUtil.DOT, true));
            data.put("template", sw.toString());
            dataMap.add(data);
        }
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     *
     * @param id Id
     * @return 数据
     */
    @Override
    public byte[] downloadCode(Long id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(id, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param id Id
     */
    @Override
    public void generatorCode(Long id) {
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table, Boolean.FALSE);
                FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetUtil.UTF_8);
            } catch (IOException e) {
                AjaxResult.warn(StrUtil.format("渲染模板失败，表名：{}", table.getName()));
            }
        }
    }

    /**
     * 批量生成代码（下载方式）
     *
     * @param ids Ids数组
     * @return 数据
     */
    @Override
    public byte[] downloadCode(Long[] ids) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Long id : ids) {
            generatorCode(id, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     *
     * @param id  Id
     * @param zip 压缩包流
     */
    private void generatorCode(Long id, ZipOutputStream zip) {
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            try {
                String fileUrl = VelocityUtils.getFileName(template, table, Boolean.TRUE);
                // 添加到zip
                zip.putNextEntry(new ZipEntry(fileUrl));
                IOUtils.write(sw.toString(), zip, HttpConstants.Character.UTF8.getCode());
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getName(), e);
            }
        }
    }

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTableDto genTable) {
        JSONObject optionsObj = JSON.parseObject(genTable.getOptions());
        checkTclBasic(genTable, optionsObj);
        switch (TemplateType.getByCode(genTable.getTplCategory())) {
            case TREE:
                checkTclTree(optionsObj);
            case BASE:
                checkTclBase(optionsObj);
        }
    }

    /**
     * 校验基础表配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclBasic(GenTableDto genTable, JSONObject optionsObj) {
        checkSourceMode(genTable, optionsObj);
        checkCommonMode(genTable, optionsObj);

    }

    /**
     * 校验基础源策略模式配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkSourceMode(GenTableDto genTable, JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.SOURCE_MODE.getCode()))) {
            AjaxResult.warn("未设置源策略模式！");
        }
        if (StrUtil.isNotEmpty(optionsObj.getString(OptionField.IS_TENANT.getCode())) && StrUtil.equals(optionsObj.getString(OptionField.IS_TENANT.getCode()), DictConstants.DicYesNo.YES.getCode())) {
            for (GenTableColumnDto column : genTable.getSubList()) {
                if (ArrayUtil.contains(GenConfig.getEntity().getBack().getTenant(), column.getJavaField())) {
                    return;
                }
            }
            AjaxResult.warn("未在业务表中发现多租户关键字，请关闭多租户模式重试！");
        }
    }

    /**
     * 校验数据混合模式配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkCommonMode(GenTableDto genTable, JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.COMMON_MODE.getCode()))) {
            // 暂不开启 | 兼容vue2 next-ui变更
//            AjaxResult.warn("未设置数据混合模式！");
            return;
        }
        if (StrUtil.isNotEmpty(optionsObj.getString(OptionField.IS_COMMON.getCode())) && StrUtil.equals(optionsObj.getString(OptionField.IS_COMMON.getCode()), DictConstants.DicYesNo.YES.getCode())) {
            for (GenTableColumnDto column : genTable.getSubList()) {
                if (ArrayUtil.contains(GenConfig.getEntity().getBack().getCommon(), column.getJavaField())) {
                    return;
                }
            }
            AjaxResult.warn("未在业务表中发现公共数据关键字，请关闭数据混合模式重试！");
        }
    }

    /**
     * 校验单表配置
     *
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclBase(JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_MODULE_ID.getCode()))) {
            AjaxResult.warn("归属模块不能为空");
        } else if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_MENU_ID.getCode()))) {
            AjaxResult.warn("归属菜单不能为空");
        }
    }

    /**
     * 校验树表配置
     *
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclTree(JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.TREE_ID.getCode()))) {
            AjaxResult.warn("树编码字段不能为空");
        } else if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_ID.getCode()))) {
            AjaxResult.warn("树父编码字段不能为空");
        } else if (StrUtil.isEmpty(optionsObj.getString(OptionField.TREE_NAME.getCode()))) {
            AjaxResult.warn("树名称字段不能为空");
        } else if (StrUtil.isEmpty(optionsObj.getString(OptionField.ANCESTORS.getCode()))) {
            AjaxResult.warn("树祖籍列表字段不能为空");
        }
    }

    /**
     * 初始化代码生成表数据
     *
     * @param id Id
     * @return 业务表对象
     */
    private GenTableDto initTable(Long id) {
        GenTableDto table = selectById(id);
        JSONObject optionsObj = JSON.parseObject(table.getOptions());
        // 设置列信息
        switch (TemplateType.getByCode(table.getTplCategory())) {
            case TREE:
                setTreeTable(table, optionsObj);
            case BASE:
                setBaseTable(table, optionsObj);
                setMenuOptions(table, optionsObj);
        }
        return table;
    }

    /**
     * 设置基础表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setBaseTable(GenTableDto table, JSONObject optionsObj) {
        table.getSubList().forEach(column -> {
            if (column.getIsPk()) {
                table.setPkColumn(column);
            }
        });
    }

    /**
     * 设置树表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setTreeTable(GenTableDto table, JSONObject optionsObj) {
    }

    /**
     * 设置菜单信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setMenuOptions(GenTableDto table, JSONObject optionsObj) {
        Long menuId = optionsObj.getLong(OptionField.PARENT_MENU_ID.getCode());
        if (ObjectUtil.equals(MENU_TOP_NODE, menuId)) {
            optionsObj.put(OptionField.PARENT_MENU_ANCESTORS.getCode(), menuId);
        } else {
            R<SysMenuDto> result = remoteMenuService.getInfoInner(menuId);
            if (result.isFail()) {
                AjaxResult.warn("菜单服务异常，请联系管理员！");
            } else if (ObjectUtil.isNull(result.getData())) {
                AjaxResult.warn("该服务对应的菜单已被删除，请先修改后再生成代码！");
            }
            SysMenuDto menu = result.getData();
            if (StrUtil.isEmpty(menu.getAncestors())) {
                optionsObj.put(OptionField.PARENT_MENU_ANCESTORS.getCode(), menu.getId());
            } else {
                optionsObj.put(OptionField.PARENT_MENU_ANCESTORS.getCode(), menu.getAncestors() + "," + menu.getId());
            }
        }
        table.setOptions(optionsObj.toString());
    }
}
