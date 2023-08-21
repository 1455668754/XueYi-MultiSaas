package com.xueyi.common.web.correlate.domain;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;
import com.xueyi.common.web.entity.mapper.BasicMapper;
import com.xueyi.common.web.entity.service.IBaseService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 间接关联映射对象
 *
 * @author xueyi
 */
@Data
@SuppressWarnings({"rawtypes"})
@EqualsAndHashCode(callSuper = true)
public final class Indirect<D extends BaseEntity, M extends BasisEntity, S extends BaseEntity> extends BaseCorrelate<Indirect.ORM> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 关联类型 */
    private final CorrelateConstants.SubTableType relationType = CorrelateConstants.SubTableType.INDIRECT;

    /**
     * 构建间接关联映射
     *
     * @param mergeMapper  中间关联Mapper接口
     * @param mergeMainFun 中间关联主键
     * @param mainIdFun    关联主键
     */
    public Indirect(CorrelateConstants.SubOperate operateType, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<D, ?> mainIdFun) {
        if (ObjectUtil.notEqual(CorrelateConstants.SubOperate.DELETE, operateType)) {
            throw new UtilException("仅允许删除方法使用！");
        }
        initIndirect(operateType, mergeMapper, mergeMainFun, mainIdFun);
    }

    /**
     * 构建间接关联映射
     *
     * @param mergeMapper  中间关联Mapper接口
     * @param mergeMainFun 中间关联主键
     * @param mainIdFun    关联主键
     */
    public Indirect(CorrelateConstants.SubOperate operateType, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<M, ?> mergeSlaveFun, SFunction<D, ?> mainIdFun, SFunction<D, ?> subKeyFun) {
        initIndirect(operateType, mergeMapper, mergeMainFun, mainIdFun);
        this.orm.setMergeSlaveField(CorrelateUtil.getField(mergeSlaveFun));
        setSubKeyFun(subKeyFun);
    }

    /**
     * 构建间接关联映射
     *
     * @param slaveService  关联Service接口
     * @param mergeMapper   中间关联Mapper接口
     * @param mergeMainFun  中间关联主键
     * @param mergeSlaveFun 中间关联从键
     * @param mainIdFun     关联主键
     * @param slaveIdFun    关联从键
     */
    public Indirect(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<M, ?> mergeSlaveFun, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun) {
        initIndirect(operateType, slaveService, mergeMapper, mergeMainFun, mergeSlaveFun, mainIdFun, slaveIdFun);
    }

    /**
     * 构建间接关联映射
     *
     * @param slaveService  关联Service接口
     * @param mergeMapper   中间关联Mapper接口
     * @param mergeMainFun  中间关联主键
     * @param mergeSlaveFun 中间关联从键
     * @param mainIdFun     关联主键
     * @param slaveIdFun    关联从键
     * @param relations     子关联映射
     */
    public Indirect(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<M, ?> mergeSlaveFun, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, List<? extends BaseCorrelate> relations) {
        initIndirect(operateType, slaveService, mergeMapper, mergeMainFun, mergeSlaveFun, mainIdFun, slaveIdFun);
        this.relations = relations;
    }

    /**
     * 构建间接关联映射
     *
     * @param slaveService  关联Service接口
     * @param mergeMapper   中间关联Mapper接口
     * @param mergeMainFun  中间关联主键
     * @param mergeSlaveFun 中间关联从键
     * @param mainIdFun     关联主键
     * @param slaveIdFun    关联从键
     * @param subKeyFun     从数据关联从键
     * @param subInfoFun    从数据关联从对象
     */
    public Indirect(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<M, ?> mergeSlaveFun, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, SFunction<D, ?> subKeyFun, SFunction<D, ?> subInfoFun) {
        initIndirect(operateType, slaveService, mergeMapper, mergeMainFun, mergeSlaveFun, mainIdFun, slaveIdFun);
        setSubKeyFun(subKeyFun);
        setSubInfoFun(subInfoFun);
    }

    /**
     * 构建间接关联映射
     *
     * @param slaveService  关联Service接口
     * @param mergeMapper   中间关联Mapper接口
     * @param mergeMainFun  中间关联主键
     * @param mergeSlaveFun 中间关联从键
     * @param mainIdFun     关联主键
     * @param slaveIdFun    关联从键
     * @param subKeyFun     从数据关联从键
     * @param subInfoFun    从数据关联从对象
     * @param relations     子关联映射
     */
    public Indirect(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<M, ?> mergeSlaveFun, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun, SFunction<D, ?> subKeyFun, SFunction<D, ?> subInfoFun, List<? extends BaseCorrelate> relations) {
        initIndirect(operateType, slaveService, mergeMapper, mergeMainFun, mergeSlaveFun, mainIdFun, slaveIdFun);
        setSubKeyFun(subKeyFun);
        setSubInfoFun(subInfoFun);
        this.relations = relations;
    }

    /**
     * 构建间接关联映射
     *
     * @param slaveService  关联Service接口
     * @param mergeMapper   中间关联Mapper接口
     * @param mergeMainFun  中间关联主键
     * @param mergeSlaveFun 中间关联从键
     * @param mainIdFun     关联主键
     * @param slaveIdFun    关联从键
     */
    private void initIndirect(CorrelateConstants.SubOperate operateType, Class<? extends IBaseService> slaveService, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<M, ?> mergeSlaveFun, SFunction<D, ?> mainIdFun, SFunction<S, ?> slaveIdFun) {
        initIndirect(operateType, mergeMapper, mergeMainFun, mainIdFun);
        this.orm.setSlaveService(slaveService);
        this.orm.setSlaveKeyField(CorrelateUtil.getField(slaveIdFun));
        this.orm.setMergeSlaveField(CorrelateUtil.getField(mergeSlaveFun));
    }

    /**
     * 构建间接关联映射
     *
     * @param mergeMapper  中间关联Mapper接口
     * @param mergeMainFun 中间关联主键
     * @param mainIdFun    关联主键
     */
    private void initIndirect(CorrelateConstants.SubOperate operateType, Class<? extends BasicMapper<M>> mergeMapper, SFunction<M, ?> mergeMainFun, SFunction<D, ?> mainIdFun) {
        this.orm = new ORM();
        this.operateType = operateType;
        this.orm.setMergeMapper(mergeMapper);
        this.orm.setMainKeyField(CorrelateUtil.getField(mainIdFun));
        this.orm.setMergeMainField(CorrelateUtil.getField(mergeMainFun));
        this.orm.setMergeInfoClazz(CorrelateUtil.getClass(mergeMainFun));
    }

    /**
     * 设置从数据关联从键
     *
     * @param subKeyFun 从数据关联从键
     */
    private void setSubKeyFun(SFunction<D, ?> subKeyFun) {
        this.orm.setSubKeyField(CorrelateUtil.getField(subKeyFun));
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

        /** 中间关联 - Mapper接口 */
        private Class<? extends BasicMapper> mergeMapper;

        /** 中间关联类 - Class */
        private Class<?> mergeInfoClazz;

        /** 中间关联主键 - Field */
        private Field mergeMainField;

        /** 中间关联主键 - 数据库字段名 */
        private String mergeMainSqlName;

        /** 中间关联从键 - Field */
        private Field mergeSlaveField;

        /** 中间关联从键 - 数据库字段名 */
        private String mergeSlaveSqlName;

        /** 从数据关联从键 - Field */
        private Field subKeyField;

        /** 从数据关联数据类型为数组（true是 false否） */
        private Boolean isArray;
    }
}
