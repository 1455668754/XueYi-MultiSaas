package com.xueyi.common.web.entity.manager.impl.handle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 数据封装层处理 操作方法 基类通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <P>  Po
 * @param <PM> PoMapper
 * @author xueyi
 */

public class BaseHandleManager<Q extends P, D extends P, P extends BaseEntity, PM extends BaseMapper<Q, D, P>, CT extends BaseConverter<Q, D, P>> {

    @Autowired
    protected PM baseMapper;

    @Autowired
    protected CT baseConverter;

    /** Dto泛型的类型 */
    @SuppressWarnings("unchecked")
    private final Class<D> DClass = (Class<D>) getClazz(1);

    /** Po泛型的类型 */
    @SuppressWarnings("unchecked")
    private final Class<P> PClass = (Class<P>) getClazz(2);

    public Class<D> getDClass() {
        return DClass;
    }

    public Class<P> getPClass() {
        return PClass;
    }

    /**
     * 获取class
     *
     * @return class
     */
    protected Type getClazz(int index) {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return pType.getActualTypeArguments()[index];
        }
        return null;
    }

    /**
     * 查询条件附加
     *
     * @param selectType   查询类型
     * @param queryWrapper 条件构造器
     * @param query        数据查询对象
     */
    protected void SelectListQuery(BaseConstants.SelectType selectType, LambdaQueryWrapper<P> queryWrapper, Q query) {
    }
}
