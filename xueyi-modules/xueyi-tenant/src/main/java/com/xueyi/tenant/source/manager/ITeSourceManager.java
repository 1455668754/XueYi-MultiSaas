package com.xueyi.tenant.source.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;

/**
 * 数据源管理 数据封装层
 *
 * @author xueyi
 */
public interface ITeSourceManager extends IBaseManager<TeSourceQuery, TeSourceDto> {
}