package com.xueyi.tenant.source.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;

/**
 * 租户服务 | 策略模块 | 数据源管理 服务层
 *
 * @author xueyi
 */
public interface ITeSourceService extends IBaseService<TeSourceQuery, TeSourceDto> {

    /**
     * 校验数据源是否为默认数据源
     *
     * @param id 数据源id
     * @return 结果 | true/false 是/不是
     */
    boolean checkIsDefault(Long id);
}