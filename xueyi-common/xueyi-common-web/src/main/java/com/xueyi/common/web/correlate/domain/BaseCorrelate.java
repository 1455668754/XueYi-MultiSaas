package com.xueyi.common.web.correlate.domain;

import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 从属关联关系定义对象
 *
 * @author xueyi
 */
@Data
public sealed class BaseCorrelate<CorrelateORM extends BaseCorrelate.ORM> implements Serializable permits Direct, Indirect, Remote {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 校验状态 */
    protected Boolean isPassed = Boolean.FALSE;

    /** 分组名称 */
    protected String groupName;

    /** 关联类型 */
    protected CorrelateConstants.SubTableType relationType;

    /** 操作类型 */
    protected CorrelateConstants.SubOperate operateType;

    /** 删除类型 */
    protected CorrelateConstants.SubDeleteType deleteType;

    /** 子关联映射 */
    protected List<? extends BaseCorrelate> relations;

    /** 关联数据映射 */
    protected CorrelateORM orm;

    /**
     * 关联映射执行对象
     *
     * @author xueyi
     */
    @Data
    public sealed static class ORM implements Serializable permits Direct.ORM, Indirect.ORM, Remote.ORM {

        @Serial
        private static final long serialVersionUID = 1L;

        /** 关联主数据 - Field */
        protected Field mainKeyField;

        /** 关联主数据 - 数据库字段名 */
        protected String mainKeySqlName;

        /** 关联从数据 - Field */
        protected Field slaveKeyField;

        /** 关联从数据 - 数据库字段名 */
        protected String slaveKeySqlName;

        /** 从数据关联从对象 - Field */
        protected Field subInfoField;

        /** 从数据关联数据类型 */
        protected CorrelateConstants.DataRow subDataRow;

        /** 间接关联类型 */
        protected CorrelateConstants.MergeType mergeType = CorrelateConstants.MergeType.DIRECT;
    }
}
