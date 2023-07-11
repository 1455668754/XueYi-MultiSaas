package com.xueyi.tenant.source.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;
import com.xueyi.tenant.source.domain.model.TeStrategyConverter;
import com.xueyi.tenant.api.source.domain.po.TeStrategyPo;
import com.xueyi.tenant.api.source.domain.query.TeStrategyQuery;
import com.xueyi.tenant.source.manager.ITeStrategyManager;
import com.xueyi.tenant.source.mapper.TeStrategyMapper;
import org.springframework.stereotype.Component;


/**
 * 租户服务 | 策略模块 | 源策略管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class TeStrategyManagerImpl extends BaseManagerImpl<TeStrategyQuery, TeStrategyDto, TeStrategyPo, TeStrategyMapper, TeStrategyConverter> implements ITeStrategyManager {

    /**
     * 校验数据源是否被使用
     *
     * @param sourceId 数据源id
     * @return 结果
     */
    @Override
    public TeStrategyDto checkSourceExist(Long sourceId) {
        TeStrategyPo strategy = baseMapper.selectOne(
                Wrappers.<TeStrategyPo>query().lambda()
                        .eq(TeStrategyPo::getSourceId, sourceId)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(strategy);
    }
}