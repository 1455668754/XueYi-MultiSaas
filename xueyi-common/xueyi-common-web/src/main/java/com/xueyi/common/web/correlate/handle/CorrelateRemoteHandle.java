package com.xueyi.common.web.correlate.handle;

import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.ClassUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.domain.Remote;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 主从关联数据处理 | 远程关联
 *
 * @author xueyi
 */
@Slf4j
@SuppressWarnings({"unchecked"})
public final class CorrelateRemoteHandle extends CorrelateBaseHandle {

    /**
     * 组装数据对象关联数据 | 远程关联
     *
     * @param dto    数据对象
     * @param remote 远程关联映射对象
     */
    public static <D extends BaseEntity, S extends BaseEntity> void assembleRemoteObj(D dto, Remote<D, S> remote) {
        assembleRemoteBuild(dto, null, remote);
    }

    /**
     * 组装集合关联数据 | 远程关联
     *
     * @param dtoList 数据对象集合
     * @param remote  远程关联映射对象
     */
    public static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> void assembleRemoteList(Coll dtoList, Remote<D, S> remote) {
        assembleRemoteBuild(null, dtoList, remote);
    }

    /**
     * 查询关联数据 | 数据组装 | 远程关联
     *
     * @param dto     数据对象
     * @param dtoList 数据对象集合
     * @param remote  远程关联映射对象
     */
    private static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> void assembleRemoteBuild(D dto, Coll dtoList, Remote<D, S> remote) {
        Remote.ORM ormRemote = remote.getOrm();
        Set<Object> findInSet = ObjectUtil.isNotNull(dto)
                ? getFieldKeys(dto, ormRemote, ormRemote.getMainKeyField())
                : getFieldKeys(dtoList, ormRemote, ormRemote.getMainKeyField());
        if (isEmpty(findInSet)) {
            return;
        }
        // 子查询进行数据关联操作
        R<List<S>> subListR = SpringUtil.getBean(ormRemote.getRemoteService()).selectListByIdsInner(findInSet);
        resultFail(subListR);
        if (ObjectUtil.isNotNull(dto)) {
            setSubField(dto, subListR.getData(), ormRemote.getSubDataRow(), ormRemote.getMergeType(), ormRemote.getMainKeyField(), ormRemote.getSlaveKeyField(), ormRemote.getSubInfoField());
        } else if (CollUtil.isNotEmpty(dtoList)) {
            setSubField(dtoList, subListR.getData(), ormRemote.getSubDataRow(), ormRemote.getMergeType(), ormRemote.getMainKeyField(), ormRemote.getSlaveKeyField(), ormRemote.getSubInfoField());
        }
    }

    /**
     * 校验关联映射是否合规
     *
     * @param remote 远程关联映射对象
     */
    public static <D extends BaseEntity, S extends BaseEntity> void checkORMLegal(Remote<D, S> remote) {
        Remote.ORM ormRemote = remote.getOrm();
        if (ObjectUtil.isNull(ormRemote.getRemoteService())) {
            logReturn(StrUtil.format("groupName: {}, remoteService can not be null", remote.getGroupName()));
        }
        ormRemote.setMergeType(ClassUtil.isSimpleType(ormRemote.getMainKeyField().getType()) ? CorrelateConstants.MergeType.DIRECT : CorrelateConstants.MergeType.INDIRECT);
        if (ObjectUtil.isNull(ormRemote.getSlaveKeyField()) || ClassUtil.isNotSimpleType(ormRemote.getSlaveKeyField().getType())) {
            logReturn(StrUtil.format("groupName: {}, slaveKeyField can not be null or not BasicType", remote.getGroupName()));
        } else if (ObjectUtil.isNotNull(ormRemote.getSubDataRow()) && ObjectUtil.equals(CorrelateConstants.DataRow.SINGLE, ormRemote.getSubDataRow()) && ormRemote.getMergeType().isIndirect()) {
            logReturn(StrUtil.format("groupName: {}, subInfoField is single, but mainKeyField is Collection", remote.getGroupName()));
        }
    }

    /**
     * 远程结果校验
     *
     * @param resultR 远程调用返回结果
     */
    private static void resultFail(R<?> resultR) {
        if (resultR.isFail()) {
            throw new ServiceException(resultR.getMsg());
        }
    }
}
