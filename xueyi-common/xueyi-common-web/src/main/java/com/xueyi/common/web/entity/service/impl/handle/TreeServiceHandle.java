package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 服务层 操作方法 树型实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <C>   Correlate
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class TreeServiceHandle<Q extends TreeEntity<D>, D extends TreeEntity<D>, C extends Enum<? extends Enum<?>> & CorrelateService, IDG extends ITreeManager<Q, D>> extends BaseServiceImpl<Q, D, C, IDG> {

    /**
     * 单条操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newDto  新数据对象（删除时不存在）
     * @param id      Id集合（非删除时不存在）
     */
    @Override
    protected D startHandle(OperateConstants.ServiceType operate, D newDto, Serializable id) {
        D originDto = super.startHandle(operate, newDto, id);
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
        return originDto;
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    @Override
    protected void endHandle(OperateConstants.ServiceType operate, int row, D originDto, D newDto) {
        if (row <= 0)
            return;
        switch (operate) {
            case ADD -> {
                // insert correlate data
                addCorrelates(newDto, getBasicCorrelate(CorrelateConstants.ServiceType.ADD));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case EDIT -> {
                // update children data ancestors && level ?.status
                if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), newDto.getStatus())) {
                    baseManager.updateChildren(newDto);
                } else if (StrUtil.notEquals(newDto.getAncestors(), newDto.getOldAncestors())) {
                    baseManager.updateChildrenAncestors(newDto);
                }
                // update correlate data
                editCorrelates(originDto, newDto, getBasicCorrelate(CorrelateConstants.ServiceType.EDIT));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case EDIT_STATUS -> {
                // update children data ?.status
                if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), newDto.getStatus())) {
                    baseManager.updateChildren(newDto);
                }
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case DELETE -> {
                // 此处注意：当前Id已被删除，故仅能查询出子节点数据
                List<D> childList = baseManager.selectChildListById(originDto.getId());
                // delete children data
                baseManager.deleteChildByAncestors(originDto.getChildAncestors());
                // 将当前节点加入变更列表
                childList.add(originDto);
                // delete correlate data
                delCorrelates(childList, getBasicCorrelate(CorrelateConstants.ServiceType.DELETE));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REMOVE, childList);
            }
        }
    }

    /**
     * 批量操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newList 新数据对象集合（删除时不存在）
     * @param idList  Id集合（非删除时不存在）
     */
    @Override
    protected List<D> startBatchHandle(OperateConstants.ServiceType operate, Collection<D> newList, Collection<? extends Serializable> idList) {
        List<D> originList = super.startBatchHandle(operate, newList, idList);
        switch (operate) {
            case BATCH_EDIT:
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
            case BATCH_ADD:
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
        return originList;
    }

    /**
     * 批量操作 - 结束处理
     *
     * @param operate    服务层 - 操作类型
     * @param rows       操作数据条数
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    @Override
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<D> originList, Collection<D> newList) {
        if (rows <= 0)
            return;
        switch (operate) {
            case BATCH_ADD -> {
                // insert correlate data
                addCorrelates(newList, getBasicCorrelate(CorrelateConstants.ServiceType.BATCH_ADD));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newList);
            }
            case BATCH_EDIT -> {
                // update children data ancestors && level ?.status
                newList.forEach(item -> {
                    if (ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), item.getStatus())) {
                        baseManager.updateChildren(item);
                    } else if (StrUtil.notEquals(item.getAncestors(), item.getOldAncestors())) {
                        baseManager.updateChildrenAncestors(item);
                    }
                });
                // update correlate data
                editCorrelates(originList, newList, getBasicCorrelate(CorrelateConstants.ServiceType.BATCH_EDIT));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newList);
            }
            case BATCH_DELETE -> {
                List<Long> idList = originList.stream().map(BasisEntity::getId).toList();
                // 此处注意：当前Ids已被删除，故仅能查询出子节点数据
                List<D> childList = baseManager.selectChildListByIds(idList);
                String[] childAncestors = childList.stream().map(D::getChildAncestors).toArray(String[]::new);
                // delete children data
                baseManager.deleteChildByAncestors(childAncestors);
                // 将当前节点加入变更列表
                childList.addAll(originList);
                // delete correlate data
                delCorrelates(childList, getBasicCorrelate(CorrelateConstants.ServiceType.BATCH_DELETE));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REMOVE, originList);
            }
        }
    }
}
