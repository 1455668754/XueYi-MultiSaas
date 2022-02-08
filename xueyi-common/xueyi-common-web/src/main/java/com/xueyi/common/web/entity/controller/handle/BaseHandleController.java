package com.xueyi.common.web.entity.controller.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.entity.BaseEntity;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.common.web.entity.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 操作层 操作方法 基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @author xueyi
 */
public abstract class BaseHandleController<D extends BaseEntity, DS extends IBaseService<D>> extends BasisController {

    @Autowired
    protected DS baseService;

    protected Class<D> tClass;

    /** 定义节点名称 */
    protected abstract String getNodeName();

    /**
     * 前置校验 （强制）增加/修改
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void baseRefreshValidated(BaseConstants.Operate operate, D d) {
    }

    /**
     * 前置校验 （强制）修改状态
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void baseEditStatusValidated(BaseConstants.Operate operate, D d) {
    }

    /**
     * 前置校验 （强制）删除
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param idList  Id集合
     */
    protected void baseRemoveValidated(BaseConstants.Operate operate, List<Long> idList) {
    }

    /**
     * 删除 空校验
     *
     * @param idList 待删除Id集合
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemove(List)
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemoveForce(List)
     */
    protected void removeNullValidated(List<Long> idList) {
        if (CollUtil.isEmpty(idList))
            throw new ServiceException(StrUtil.format("无待删除{}！", getNodeName()));
    }
}
