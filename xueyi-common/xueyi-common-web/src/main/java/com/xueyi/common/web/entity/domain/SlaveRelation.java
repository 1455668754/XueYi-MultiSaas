package com.xueyi.common.web.entity.domain;

import com.xueyi.common.core.constant.basic.OperateConstants.SubDeleteType;
import com.xueyi.common.core.constant.basic.OperateConstants.SubOperateLimit;
import com.xueyi.common.core.constant.basic.OperateConstants.SubTableType;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.mapper.BasicMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * 从属关联关系定义对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
public class SlaveRelation {

    /** 分组名称 */
    private String groupName;

    /** 关联类型 */
    private SubTableType relationType;

    /** 删除类型 */
    private SubDeleteType deleteType;

    /** 主数据对象 - 主数据Dto对象class */
    private Class<? extends BaseEntity> mainDtoClass;

    /** 从数据 - 方法类class */
    private Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> slaveClass;

    /** 主数据对象 - 主键字段 */
    private Field mainIdField;

    /** 主数据对象 - 关联键字段 */
    private Field mainField;

    /** 从数据对象 - 关联键字段 */
    private Field slaveField;

    /** 从数据对象 - 关联键字段 - 数据库字段名 */
    private String slaveFieldSqlName;

    /** 主数据对象 - 关联数据接收字段 */
    private Field receiveField;

    /** 间接关联 - 中间类class */
    private Class<? extends BasicMapper<? extends BasisEntity>> mergeClass;

    /** 中间数据对象 - 间接关联 - 中间类数据对象class */
    private Class<? extends BasisEntity> mergePoClass;

    /** 中间数据对象 - 间接关联 - 主数据关联键字段 */
    private Field mergeMainField;

    /** 中间数据对象 - 间接关联 - 主数据关联键字段 - 数据库字段名 */
    private String mergeMainFieldSqlName;

    /** 中间数据对象 - 间接关联 - 从数据关联键字段 */
    private Field mergeSlaveField;

    /** 中间数据对象 - 间接关联 - 从数据关联键字段 - 数据库字段名 */
    private String mergeSlaveFieldSqlName;

    /** 主数据对象 - 间接关联 - 关联数据键值接收字段 */
    private Field receiveArrField;

    /** 查询操作 */
    private Boolean isSelect;

    /** 新增操作 */
    private Boolean isAdd;

    /** 修改操作 */
    private Boolean isEdit;

    /** 删除操作 */
    private Boolean isDelete;


    public SlaveRelation(String groupName,
                         Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> slaveClass,
                         SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.slaveClass = slaveClass;
        this.relationType = SubTableType.DIRECT;
        this.deleteType = SubDeleteType.NORMAL;
        initOperate(operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> slaveClass,
                         SubDeleteType deleteType,
                         SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.slaveClass = slaveClass;
        this.relationType = SubTableType.DIRECT;
        this.deleteType = deleteType;
        initOperate(operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> slaveClass,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.slaveClass = slaveClass;
        this.mergeClass = mergeClass;
        this.mergePoClass = mergePoClass;
        this.relationType = SubTableType.INDIRECT;
        this.deleteType = SubDeleteType.NORMAL;
        initOperate(operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.mergeClass = mergeClass;
        this.mergePoClass = mergePoClass;
        this.relationType = SubTableType.INDIRECT;
        this.deleteType = SubDeleteType.NORMAL;
        initOperate(operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> slaveClass,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubDeleteType deleteType,
                         SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.slaveClass = slaveClass;
        this.mergeClass = mergeClass;
        this.mergePoClass = mergePoClass;
        this.relationType = SubTableType.INDIRECT;
        this.deleteType = deleteType;
        initOperate(operateTypes);
    }

    /** 初始化操作 */
    public void initOperate(SubOperateLimit... operateTypes) {
        if (ArrayUtil.isNotEmpty(operateTypes)) {
            for (SubOperateLimit operateType : operateTypes) {
                switch (operateType) {
                    case EX_SEL -> this.isSelect = Boolean.FALSE;
                    case EX_ADD -> this.isAdd = Boolean.FALSE;
                    case EX_EDIT -> this.isEdit = Boolean.FALSE;
                    case EX_DEL -> this.isDelete = Boolean.FALSE;
                    case ONLY_DEL -> {
                        this.isSelect = Boolean.FALSE;
                        this.isAdd = Boolean.FALSE;
                        this.isEdit = Boolean.FALSE;
                    }
                    case EX_SEL_OR_EDIT -> {
                        this.isSelect = Boolean.FALSE;
                        this.isEdit = Boolean.FALSE;
                    }
                    case EX_ADD_OR_EDIT -> {
                        this.isAdd = Boolean.FALSE;
                        this.isEdit = Boolean.FALSE;
                    }
                    case ONLY_SEL -> {
                        this.isAdd = Boolean.FALSE;
                        this.isEdit = Boolean.FALSE;
                        this.isDelete = Boolean.FALSE;
                    }
                    default -> {
                    }
                }
            }
        }
        if (ObjectUtil.isNull(this.isSelect))
            this.isSelect = Boolean.TRUE;
        if (ObjectUtil.isNull(this.isAdd))
            this.isAdd = Boolean.TRUE;
        if (ObjectUtil.isNull(this.isEdit))
            this.isEdit = Boolean.TRUE;
        if (ObjectUtil.isNull(this.isDelete))
            this.isDelete = Boolean.TRUE;
    }
}
