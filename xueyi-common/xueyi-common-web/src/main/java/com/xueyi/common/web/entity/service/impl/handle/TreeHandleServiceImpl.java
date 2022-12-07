package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * 单条操作 - 开始处理
     *
     * @param operate   服务层 - 操作类型
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void startHandle(OperateConstants.ServiceType operate, D originDto, D newDto) {
        switch (operate) {
            case EDIT:
                newDto.setOldAncestors(originDto.getAncestors());
                newDto.setOldLevel(originDto.getLevel());
            case ADD:
                if (ObjectUtil.equals(BaseConstants.TOP_ID, newDto.getParentId())) {
                    newDto.setAncestors(String.valueOf(BaseConstants.TOP_ID));
                    newDto.setLevel(NumberUtil.One);
                } else {
                    D parent = baseManager.selectById(newDto.getParentId());
                    newDto.setAncestors(parent.getAncestors() + StrUtil.COMMA + newDto.getParentId());
                    newDto.setLevel(parent.getLevel() + NumberUtil.One);
                }
                break;
        }
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate 服务层 - 操作类型
     * @param row     操作数据条数
     * @param dto     数据对象
     */
    protected void endHandle(OperateConstants.ServiceType operate, int row, D dto) {
        if (row <= 0)
            return;
        switch (operate) {
            case EDIT -> {
                if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), dto.getStatus())) {
                    baseManager.updateChildren(dto);
                } else {
                    baseManager.updateChildrenAncestors(dto);
                }
            }
            case EDIT_STATUS -> {
                if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), dto.getStatus())) {
                    baseManager.updateChildrenStatus(dto);
                }
            }
            case DELETE -> baseManager.deleteChildren(dto.getId());
        }
        super.endHandle(operate, row, dto);
    }

    /**
     * 批量操作 - 开始处理
     *
     * @param operate    服务层 - 操作类型
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    protected void startBatchHandle(OperateConstants.ServiceType operate, Collection<D> originList, Collection<D> newList) {
        switch (operate) {
            case EDIT:
                Map<Long, D> originMap = CollUtil.isNotEmpty(originList)
                        ? originList.stream().collect(Collectors.toMap(D::getId, Function.identity()))
                        : new HashMap<>();
                newList.forEach(item -> {
                    D originDto = originMap.get(item.getId());
                    if (ObjectUtil.isNotNull(originDto)) {
                        item.setOldAncestors(originDto.getAncestors());
                        item.setOldLevel(originDto.getLevel());
                    }
                });
            case ADD:
                Set<Long> parentIds = newList.stream().map(TreeEntity::getParentId).collect(Collectors.toSet());
                if (CollUtil.isNotEmpty(parentIds)) {
                    List<D> parentList = baseManager.selectListByIds(parentIds);
                    Map<Long, D> parentMap = parentList.stream().collect(Collectors.toMap(D::getId, Function.identity()));
                    newList.forEach(item -> {
                        if (ObjectUtil.equals(BaseConstants.TOP_ID, item.getParentId())) {
                            item.setAncestors(String.valueOf(BaseConstants.TOP_ID));
                            item.setLevel(NumberUtil.One);
                        } else {
                            D parentDto = parentMap.get(item.getParentId());
                            if (ObjectUtil.isNotNull(parentDto)) {
                                item.setAncestors(parentDto.getAncestors() + StrUtil.COMMA + parentDto.getParentId());
                                item.setLevel(parentDto.getLevel() + NumberUtil.One);
                            }
                        }
                    });
                }
                break;
        }
    }

    /**
     * 批量操作 - 结束处理
     *
     * @param operate    服务层 - 操作类型
     * @param rows       操作数据条数
     * @param entityList 数据对象集合
     */
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<D> entityList) {
        if (rows <= 0)
            return;
        switch (operate) {
            case EDIT -> entityList.forEach(item -> {
                if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), item.getStatus())) {
                    baseManager.updateChildren(item);
                } else {
                    baseManager.updateChildrenAncestors(item);
                }
            });
            case EDIT_STATUS -> entityList.forEach(item -> {
                if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), item.getStatus())) {
                    baseManager.updateChildrenStatus(item);
                }
            });
            case BATCH_DELETE -> {
                for (D dto : entityList)
                    baseManager.deleteChildren(dto.getId());
            }
        }
        super.endBatchHandle(operate, rows, entityList);
    }
}
