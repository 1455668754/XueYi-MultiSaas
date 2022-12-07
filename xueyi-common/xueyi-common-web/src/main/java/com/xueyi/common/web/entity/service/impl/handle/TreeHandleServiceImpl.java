package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;

import java.io.Serializable;

/**
 * 服务层 操作方法 树型实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class TreeHandleServiceImpl<Q extends TreeEntity<D>, D extends TreeEntity<D>, IDG extends ITreeManager<Q, D>> extends BaseServiceImpl<Q, D, IDG> {

    /**
     * 新增/修改 树型 检查父级状态
     * 是否启用，非启用则启用
     *
     * @param parentId 父级Id
     * @param status   状态
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#insert(TreeEntity)
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#update(TreeEntity)
     */
    protected void AUHandleParentStatusCheck(Serializable parentId, String status) {
        if (ObjectUtil.equals(BaseConstants.Status.NORMAL.getCode(), status)) {
            BaseConstants.Status parentStatus = checkStatus(parentId);
            if (BaseConstants.Status.DISABLE == parentStatus) {
                try {
                    D parent = getDClass().getDeclaredConstructor().newInstance();
                    parent.setId((Long) parentId);
                    parent.setStatus(BaseConstants.Status.NORMAL.getCode());
                    baseManager.updateStatus(parent);
                }catch (Exception ignored) {}
            }
        }
    }

    /**
     * 修改状态 树型 检查父级状态
     * 是否启用，非启用则启用
     *
     * @param d 数据对象
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#updateStatus
     */
    protected void USHandelParentStatusCheck(D d) {
        D nowD = baseManager.selectById(d.getId());
        if (ObjectUtil.equals(BaseConstants.Status.NORMAL.getCode(), d.getStatus())
                && BaseConstants.Status.DISABLE == checkStatus(nowD.getParentId())) {
            nowD.setStatus(BaseConstants.Status.NORMAL.getCode());
            baseManager.updateStatus(nowD);
        }
    }

    /**
     * 新增 树型 设置祖籍
     *
     * @param d 数据对象 | parentId 父Id
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#insert(TreeEntity)
     */
    protected void AHandleAncestorsSet(D d) {
        if (ObjectUtil.equals(BaseConstants.TOP_ID, d.getParentId())) {
            d.setAncestors(String.valueOf(BaseConstants.TOP_ID));
        } else {
            D parent = baseManager.selectById(d.getParentId());
            d.setAncestors(parent.getAncestors() + StrUtil.COMMA + d.getParentId());
        }
    }

    /**
     * 修改 树型 检验祖籍是否变更
     * 是否变更，变更则同步变更子节点祖籍
     *
     * @param d 数据对象 | id id | parentId 父Id
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#update(TreeEntity)
     */
    protected void UHandleAncestorsCheck(D d) {
        D original = baseManager.selectById(d.getId());
        if (!ObjectUtil.equals(d.getParentId(), original.getParentId())) {
            String oldAncestors = original.getAncestors();
            if (ObjectUtil.equals(BaseConstants.TOP_ID, d.getParentId()))
                d.setAncestors(String.valueOf(BaseConstants.TOP_ID));
            else {
                D parent = baseManager.selectById(d.getParentId());
                d.setAncestors(parent.getAncestors() + StrUtil.COMMA + d.getParentId());
            }
            baseManager.updateChildrenAncestors(d.getId(), d.getAncestors(), oldAncestors);
        }
    }

    /**
     * 修改/修改状态 树型 检查子节点状态
     * 是否变更，变更则同步禁用子节点
     *
     * @param d 数据对象
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#update(TreeEntity)
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#updateStatus
     */
    protected void UUSChildrenStatusCheck(D d) {
        D original = baseManager.selectById(d.getId());
        if (ObjectUtil.notEqual(original.getStatus(), d.getStatus())
                && ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), d.getStatus())
                && ObjectUtil.isNotNull(baseManager.checkHasNormalChild(d.getId()))) {
            baseManager.updateChildrenStatus(d.getId(), BaseConstants.Status.DISABLE.getCode());
        }
    }
}
