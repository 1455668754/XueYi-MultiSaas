package com.xueyi.common.web.entity.controller.handle;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.core.TypeUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.common.web.entity.service.IBaseService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Getter
    private final Class<Q> QClass = TypeUtil.getClazz(getClass().getGenericSuperclass(), NumberUtil.Zero);

    /** Dto泛型的类型 */
    @Getter
    private final Class<D> DClass = TypeUtil.getClazz(getClass().getGenericSuperclass(), NumberUtil.One);

    /** 定义节点名称 */
    protected abstract String getNodeName();

    /**
     * 前置校验 新增/修改
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param dto     数据对象
     */
    protected void AEHandle(BaseConstants.Operate operate, D dto) {
    }

    /**
     * 前置校验 （强制）删除
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param idList  Id集合
     */
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
    }

    /**
     * 删除 空校验
     *
     * @param idList 待删除Id集合
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemove(List)
     */
    protected void RHandleEmpty(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("无待删除{}！", getNodeName()));
        }
    }
}
