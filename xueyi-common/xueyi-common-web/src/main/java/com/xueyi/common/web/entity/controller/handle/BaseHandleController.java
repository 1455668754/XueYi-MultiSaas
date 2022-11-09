package com.xueyi.common.web.entity.controller.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.common.web.entity.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 操作层 操作方法 基类通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDS> DtoService
 * @author xueyi
 */
public abstract class BaseHandleController<Q extends BaseEntity, D extends BaseEntity, IDS extends IBaseService<Q, D>> extends BasisController {

    @Autowired
    protected IDS baseService;

    /** Query泛型的类型 */
    @SuppressWarnings("unchecked")
    private final Class<Q> QClass = (Class<Q>) getClazz(0);

    /** Dto泛型的类型 */
    @SuppressWarnings("unchecked")
    private final Class<D> DClass = (Class<D>) getClazz(1);

    /** 定义节点名称 */
    protected abstract String getNodeName();

    public Class<Q> getQClass() {
        return QClass;
    }

    public Class<D> getDClass() {
        return DClass;
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
     * 前置校验 （强制）增加/修改
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void AEHandleValidated(BaseConstants.Operate operate, D d) {
    }

    /**
     * 前置校验 （强制）修改状态
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void ESHandleValidated(BaseConstants.Operate operate, D d) {
    }

    /**
     * 前置校验 （强制）删除
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param idList  Id集合
     */
    protected void RHandleValidated(BaseConstants.Operate operate, List<Long> idList) {
    }

    /**
     * 删除 空校验
     *
     * @param idList 待删除Id集合
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemove(List)
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemoveForce(List)
     */
    protected void RHandleEmptyValidated(List<Long> idList) {
        if (CollUtil.isEmpty(idList))
            warn(StrUtil.format("无待删除{}！", getNodeName()));
    }
}
