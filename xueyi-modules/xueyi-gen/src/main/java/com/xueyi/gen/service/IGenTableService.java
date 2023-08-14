package com.xueyi.gen.service;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;

import java.util.List;

/**
 * 业务管理 服务层
 *
 * @author xueyi
 */
public interface IGenTableService extends IBaseService<GenTableQuery, GenTableDto> {

    /**
     * 查询据库列表
     *
     * @param table 业务信息
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableList(GenTableQuery table);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableListByNames(String[] tableNames, String sourceName);

    /**
     * 导入表结构
     *
     * @param tableList  导入表列表
     * @param sourceName 数据源
     */
    void importGenTable(List<GenTableDto> tableList, String sourceName);

    /**
     * 预览代码
     *
     * @param id Id
     * @return 预览数据列表
     */
    List<JSONObject> previewCode(Long id);

    /**
     * 生成代码（下载方式）
     *
     * @param id Id
     * @return 数据
     */
    byte[] downloadCode(Long id);

    /**
     * 生成代码（自定义路径）
     *
     * @param id Id
     */
    void generatorCode(Long id);

    /**
     * 批量生成代码（下载方式）
     *
     * @param ids Ids数组
     * @return 数据
     */
    byte[] downloadCode(Long[] ids);

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    void validateEdit(GenTableDto genTable);

}