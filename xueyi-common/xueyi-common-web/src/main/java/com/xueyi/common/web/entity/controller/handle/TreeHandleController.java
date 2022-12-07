package com.xueyi.common.web.entity.controller.handle;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.common.web.entity.service.ITreeService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 操作层 操作方法 树型通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDS> DtoService
 * @author xueyi
 */
public abstract class TreeHandleController<Q extends TreeEntity<D>, D extends TreeEntity<D>, IDS extends ITreeService<Q, D>> extends BaseController<Q, D, IDS> {

    /**
     * 树型 根据祖籍字段排除自己及子节点
     *
     * @param list 待排除数据对象集合
     * @see com.xueyi.common.web.entity.controller.TreeController#listExNodes(TreeEntity)
     */
    protected void SHandleExNodes(Collection<D> list, Serializable id) {
        list.removeIf(next -> ObjectUtil.equals(next.getId(), id) ||
                ArrayUtil.contains(StrUtil.splitToArray(next.getAncestors(), StrUtil.COMMA), id + StrUtil.EMPTY));
    }

    /**
     * 树型 树死循环逻辑校验 | 父节点不能为自己或自己的子节点
     *
     * @param d 待修改数据对象
     */
    protected void TreeLoopHandle(D d) {
        if (ObjectUtil.equals(d.getId(), d.getParentId()))
            warn(StrUtil.format("修改{}{}失败，上级{}不能是自己！", getNodeName(), d.getName(), getNodeName()));
        else if (baseService.checkIsChild(d.getParentId(), d.getId()))
            warn(StrUtil.format("修改{}{}失败，上级{}不能是自己的子{}！", getNodeName(), d.getName(), getNodeName(), getNodeName()));
    }

    /**
     * 树型 增加/修改 父子节点逻辑校验
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void AETreeStatusHandle(BaseConstants.Operate operate, D d) {
        if (StrUtil.equals(BaseConstants.Status.NORMAL.getCode(), d.getStatus())
                && BaseConstants.Status.DISABLE == baseService.checkStatus(d.getParentId()))
            warn(StrUtil.format("{}{}「{}」失败，该{}的父级{}已被停用，禁止启用！！",operate.getInfo(), getNodeName(), d.getName(), getNodeName(), getNodeName()));
        switch (operate) {
            case EDIT, EDIT_STATUS -> {
                if (StrUtil.equals(BaseConstants.Status.DISABLE.getCode(), d.getStatus())
                        && baseService.checkHasNormalChild(d.getId()))
                    warn(StrUtil.format("{}{}「{}」失败，该{}包含未停用的子{}，禁止禁用！", operate.getInfo(), getNodeName(), d.getName(), getNodeName(), getNodeName()));
            }
        }
    }

    /**
     * 树型 删除 子节点存在与否校验
     *
     * @param idList 待删除Id集合
     */
    protected void RHandleTreeChild(List<Long> idList) {
        int size = idList.size();
        // remove node where nodeChildren exist
        for (int i = idList.size() - 1; i >= 0; i--)
            if (baseService.checkHasChild(idList.get(i)))
                idList.remove(i);
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("删除失败，所有待删除{}皆存在子{}！", getNodeName(), getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            warn(StrUtil.format("成功删除所有无子{}的{}！", getNodeName(), getNodeName()));
        }
    }
}
