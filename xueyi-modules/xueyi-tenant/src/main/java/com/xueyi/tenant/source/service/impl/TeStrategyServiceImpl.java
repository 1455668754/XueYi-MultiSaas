package com.xueyi.tenant.source.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.source.domain.query.TeStrategyQuery;
import com.xueyi.tenant.source.manager.ITeStrategyManager;
import com.xueyi.tenant.source.service.ITeStrategyService;
import org.springframework.stereotype.Service;

/**
 * 数据源策略管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class TeStrategyServiceImpl extends BaseServiceImpl<TeStrategyQuery, TeStrategyDto, ITeStrategyManager> implements ITeStrategyService {

    /**
     * 缓存主键命名定义
     */
    @Override
    protected CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.TE_STRATEGY_KEY;
    }

    /**
     * 校验数据源是否被使用
     *
     * @param sourceId 数据源id
     * @return 结果 | true/false 存在/不存在
     */
    @Override
    public boolean checkSourceExist(Long sourceId) {
        return ObjectUtil.isNotNull(baseManager.checkSourceExist(sourceId));
    }

    /**
     * 校验源策略是否为默认源策略
     *
     * @param id 源策略id
     * @return 结果 | true/false 是/不是
     */
    @Override
    public boolean checkIsDefault(Long id) {
        TeStrategyDto strategy = baseManager.selectById(id);
        return ObjectUtil.isNotNull(strategy) && StrUtil.equals(strategy.getIsDefault(), DictConstants.DicYesNo.YES.getCode());
    }

}