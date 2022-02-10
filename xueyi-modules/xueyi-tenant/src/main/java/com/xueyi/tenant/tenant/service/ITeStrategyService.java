package com.xueyi.tenant.tenant.service;

import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.common.web.entity.service.IBaseService;

/**
 * 数据源策略管理 服务层
 *
 * @author xueyi
 */
public interface ITeStrategyService extends IBaseService<TeStrategyDto> {

    /**
     * 校验数据源是否被使用
     *
     * @param sourceId 数据源id
     * @return 结果 | true/false 存在/不存在
     */
    public boolean checkSourceExist(Long sourceId);
}