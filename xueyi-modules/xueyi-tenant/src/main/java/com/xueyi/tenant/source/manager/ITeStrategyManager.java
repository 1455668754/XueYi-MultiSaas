package com.xueyi.tenant.source.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.source.domain.query.TeStrategyQuery;


/**
 * 租户服务 | 策略模块 | 源策略管理 数据封装层
 *
 * @author xueyi
 */
public interface ITeStrategyManager extends IBaseManager<TeStrategyQuery, TeStrategyDto> {

    /**
     * 校验数据源是否被使用
     *
     * @param sourceId 数据源id
     * @return 结果
     */
    TeStrategyDto checkSourceExist(Long sourceId);
}