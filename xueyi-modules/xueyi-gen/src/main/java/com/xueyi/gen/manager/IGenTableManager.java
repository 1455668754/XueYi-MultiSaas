package com.xueyi.gen.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;

import java.util.List;

/**
 * 业务管理 数据封装层
 *
 * @author xueyi
 */
public interface IGenTableManager extends IBaseManager<GenTableQuery, GenTableDto> {

    /**
     * 查询数据库列表 | 主库
     *
     * @param table 业务对象
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableList(GenTableQuery table);

    /**
     * 查询数据库列表 | 租户库
     *
     * @param table      业务对象
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableList(GenTableQuery table, String sourceName);

    /**
     * 根据表名称组查询数据库列表 | 主库
     *
     * @param names 表名称组
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableListByNames(String[] names);

    /**
     * 根据表名称组查询数据库列表 | 租户库
     *
     * @param names      表名称组
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableListByNames(String[] names, String sourceName);
}
