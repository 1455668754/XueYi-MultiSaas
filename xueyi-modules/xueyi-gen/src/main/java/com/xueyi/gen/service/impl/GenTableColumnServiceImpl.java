package com.xueyi.gen.service.impl;

import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.gen.domain.correlate.GenTableColumnCorrelate;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.query.GenTableColumnQuery;
import com.xueyi.gen.manager.IGenTableColumnManager;
import com.xueyi.gen.service.IGenTableColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务字段管理 服务层实现
 *
 * @author xueyi
 */
@Service
public class GenTableColumnServiceImpl extends BaseServiceImpl<GenTableColumnQuery, GenTableColumnDto, GenTableColumnCorrelate, IGenTableColumnManager> implements IGenTableColumnService {

    /**
     * 根据表名称查询数据库表列信息
     *
     * @param tableName  表名称
     * @param sourceName 数据源
     * @return 数据库表列信息
     */
    @Override
    public List<GenTableColumnDto> selectDbTableColumnsByName(String tableName, String sourceName) {
        return StrUtil.isNotBlank(sourceName) && StrUtil.notEquals(TenantConstants.Source.MASTER.getCode(), sourceName)
                ? baseManager.selectDbTableColumnsByName(tableName, sourceName)
                : baseManager.selectDbTableColumnsByName(tableName);
    }
}