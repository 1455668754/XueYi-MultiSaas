package com.xueyi.gen.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.query.GenTableColumnQuery;

import java.util.List;

/**
 * 业务字段管理 数据封装层
 *
 * @author xueyi
 */
public interface IGenTableColumnManager extends IBaseManager<GenTableColumnQuery, GenTableColumnDto> {

    /**
     * 根据表名称查询数据库表列信息
     *
     * @param tableName 表名称
     * @return 数据库表列信息
     */
    List<GenTableColumnDto> selectDbTableColumnsByName(String tableName);
}
