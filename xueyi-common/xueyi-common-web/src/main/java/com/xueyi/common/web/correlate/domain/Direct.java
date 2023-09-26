package com.xueyi.common.web.correlate.domain;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;
import com.xueyi.common.web.entity.service.IBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 直接关联映射对象
 *
 * @author xueyi
 */
@Data
@SuppressWarnings({"rawtypes"})
@EqualsAndHashCode(callSuper = true)
public final class Direct<D extends BaseEntity, S extends BaseEntity> extends BaseCorrelate<Direct.ORM> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 关联类型 */
    private final CorrelateConstants.SubTableType relationType = CorrelateConstants.SubTableType.DIRECT;

    /**
     * 构建直接关联映射
     *
     * @param slaveService 关联Service接口
     * @param mainIdFun    关联主键
     * @param slaveIdFun   关联从键
     */
    public Direct(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun) {
        initDirect(operateType, slaveService, mainIdFun, slaveIdFun);
    }

    /**
     * 构建直接关联映射
     *
     * @param slaveService 关联Service接口
     * @param mainIdFun    关联主键
     * @param slaveIdFun   关联从键
     * @param relations    子关联映射
     */
    public Direct(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, List<? extends BaseCorrelate> relations) {
        initDirect(operateType, slaveService, mainIdFun, slaveIdFun);
        this.relations = relations;
    }

    /**
     * 构建直接关联映射
     *
     * @param slaveService 关联Service接口
     * @param mainIdFun    关联主键
     * @param slaveIdFun   关联从键
     * @param subInfoFun   从数据关联从对象
     */
    public Direct(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, SFunction<D, ?> subInfoFun) {
        initDirect(operateType, slaveService, mainIdFun, slaveIdFun);
        setSubInfoFun(subInfoFun);
    }

    /**
     * 构建直接关联映射
     *
     * @param slaveService 关联Service接口
     * @param mainIdFun    关联主键
     * @param slaveIdFun   关联从键
     * @param subInfoFun   从数据关联从对象
     * @param relations    子关联映射
     */
    public Direct(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, SFunction<D, ?> subInfoFun, List<? extends BaseCorrelate> relations) {
        initDirect(operateType, slaveService, mainIdFun, slaveIdFun);
        setSubInfoFun(subInfoFun);
        this.relations = relations;
    }

    /**
     * 构建直接关联映射 | 仅新增/修改
     *
     * @param slaveService 关联Service接口
     * @param mainIdFun    关联主键
     * @param slaveIdFun   关联从键
     * @param subInfoFun   从数据关联从对象
     * @param copyInfoFun  数据复制关联对象
     * @param relations    子关联映射
     */
    public Direct(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, SFunction<D, ?> subInfoFun, List<CopyFun<D, S>> copyInfoFun, List<? extends BaseCorrelate> relations) {
        initDirect(operateType, slaveService, mainIdFun, slaveIdFun);
        setSubInfoFun(subInfoFun);
        setCopyInfoFun(operateType, copyInfoFun);
        this.relations = relations;
    }

    /**
     * 初始化直接关联映射
     *
     * @param slaveService 关联Service接口
     * @param mainIdFun    关联主键
     * @param slaveIdFun   关联从键
     */
    private void initDirect(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun) {
        this.orm = new ORM();
        this.operateType = operateType;
        this.orm.setSlaveService(slaveService);
        this.orm.setMainKeyField(CorrelateUtil.getField(mainIdFun));
        this.orm.setSlaveKeyField(CorrelateUtil.getField(slaveIdFun));
    }

    /**
     * 设置从数据关联从对象
     *
     * @param subInfoFun 从数据关联从对象
     */
    private void setSubInfoFun(SFunction<D, ?> subInfoFun) {
        this.orm.setSubInfoField(CorrelateUtil.getField(subInfoFun));
    }

    /**
     * 设置从数据关联从对象
     *
     * @param copyInfoFun 数据复制关联对象
     */
    private void setCopyInfoFun(CorrelateConstants.SubOperate operateType, List<CopyFun<D, S>> copyInfoFun) {
        if (ObjectUtil.notEqual(CorrelateConstants.SubOperate.ADD, operateType) && ObjectUtil.notEqual(CorrelateConstants.SubOperate.EDIT, operateType)) {
            throw new UtilException("仅允许新增/修改方法使用！");
        }
        if (CollUtil.isNotEmpty(copyInfoFun)) {
            List<ORM.CopyField> copyFields = copyInfoFun.stream().map(item -> {
                ORM.CopyField copyField = new ORM.CopyField();
                copyField.setTargetField(CorrelateUtil.getField(item.getTargetFun()));
                copyField.setSourceField(CorrelateUtil.getField(item.getSourceFun()));
                return copyField;
            }).collect(Collectors.toList());
            this.orm.setCopyFields(copyFields);
        }
    }

    /**
     * 数据复制关联对象
     *
     * @author xueyi
     */
    @Data
    @AllArgsConstructor
    public static class CopyFun<D, S> implements Serializable {

        /** 关联主数据 - 来源对象 */
        private SFunction<D, ?> sourceFun;

        /** 关联从数据 - 目标对象 */
        private SFunction<S, ?> targetFun;

    }

    /**
     * 关联映射执行对象
     *
     * @author xueyi
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public final static class ORM extends BaseCorrelate.ORM {

        @Serial
        private static final long serialVersionUID = 1L;

        /** 关联从数据 - Service接口 */
        private Class<? extends IBaseService> slaveService;

        /** 数据复制 */
        private List<CopyField> copyFields;

        /**
         * 数据复制对象
         *
         * @author xueyi
         */
        @Data
        public static class CopyField implements Serializable {

            /** 关联主数据 - 来源对象 */
            private Field sourceField;

            /** 关联从数据 - 目标对象 */
            private Field targetField;

        }
    }
}