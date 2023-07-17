package com.xueyi.common.web.entity.manager.impl.handle;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.common.web.entity.mapper.TreeMapper;

/**
 * 数据封装层处理 操作方法 树型通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <P>  Po
 * @param <PM> PoMapper
 * @author xueyi
 */
public class TreeHandleManagerImpl<Q extends P, D extends P, P extends TreeEntity<D>, PM extends TreeMapper<Q, D, P>, CT extends BaseConverter<Q, D, P>> extends BaseManagerImpl<Q, D, P, PM, CT> {

    /**
     * 修改条件构造 | 树子数据修改
     *
     * @param dto           数据传输对象
     * @param updateWrapper 更新条件构造器
     * @return 条件构造器
     */
    protected LambdaUpdateWrapper<P> updateChildrenWrapper(D dto, LambdaUpdateWrapper<P> updateWrapper) {
        return updateWrapper;
    }
}
