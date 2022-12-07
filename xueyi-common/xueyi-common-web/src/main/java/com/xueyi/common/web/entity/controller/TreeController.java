package com.xueyi.common.web.entity.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.web.entity.controller.handle.TreeHandleController;
import com.xueyi.common.web.entity.service.ITreeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 操作层 树型通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDS> DtoService
 * @author xueyi
 */
public abstract class TreeController<Q extends TreeEntity<D>, D extends TreeEntity<D>, IDS extends ITreeService<Q, D>> extends TreeHandleController<Q, D, IDS> {

    /**
     * 查询树列表
     */
    @Override
    public AjaxResult list(Q query) {
        List<D> list = baseService.selectListScope(query);
        return success(TreeUtil.buildTree(list));
    }

    /**
     * 查询树列表（排除节点）
     */
    public AjaxResult listExNodes(Q query) {
        Serializable id = query.getId();
        query.setId(null);
        List<D> list = baseService.selectListScope(query);
        SHandleExNodes(list, id);
        return success(TreeUtil.buildTree(list));
    }

    /**
     * 树型 新增
     * 考虑父节点状态
     */
    @Override
    public AjaxResult add(@Validated({V_A.class}) @RequestBody D d) {
        d.initOperate(BaseConstants.Operate.ADD);
        AEHandle(d.getOperate(), d);
        AETreeStatusHandle(d.getOperate(), d);
        return toAjax(baseService.insert(d));
    }

    /**
     * 树型 修改
     * 考虑子节点状态
     */
    @Override
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody D d) {
        d.initOperate(BaseConstants.Operate.EDIT);
        AEHandle(d.getOperate(), d);
        TreeLoopHandle(d);
        AETreeStatusHandle(d.getOperate(), d);
        return toAjax(baseService.update(d));
    }

    /**
     * 树型 修改状态
     * 考虑子节点状态
     */
    @Override
    public AjaxResult editStatus(@RequestBody D d) {
        d.initOperate(BaseConstants.Operate.EDIT_STATUS);
        AETreeStatusHandle(d.getOperate(), d);
        return toAjax(baseService.updateStatus(d));
    }

    /**
     * 树型 批量删除
     * 考虑子节点存在与否
     */
    @Override
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        RHandleEmpty(idList);
        RHandle(BaseConstants.Operate.DELETE, idList);
        RHandleEmpty(idList);
        RHandleTreeChild(idList);
        return toAjax(baseService.deleteByIds(idList));
    }
}
