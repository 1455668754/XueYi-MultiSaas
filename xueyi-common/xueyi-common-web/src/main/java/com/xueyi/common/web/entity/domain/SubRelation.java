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
 * 子类关联对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
public class SubRelation {

    /** 子类class */
    private Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass;

    /** 分组名称 */
    private String groupName;

    /** 表关联类型 */
    private SubTableType relationType;

    /** 删除类型 */
    private SubDeleteType deleteType;

    /** 主关联键字段 */
    private Field mainKeyField;

    /** 子关联键字段 */
    private Field subKeyField;

    /** 子关联键 - 数据库字段名 */
    private String subFieldSqlName;

    /** 值接收键字段 */
    private Field receiveKeyField;

    /** 关联类class */
    private Class<? extends BasicMapper<? extends BasisEntity>> mergeClass;

    /** 关联类对象class */
    private Class<? extends BasisEntity> mergePoClass;

    /** 间接关联 - 关联主键字段 */
    private Field mergeMainKeyField;

    /** 间接关联 - 关联主键 - 数据库字段名 */
    private String mergeMainFieldSqlName;

    /** 间接关联 - 关联子键字段 */
    private Field mergeSubKeyField;

    /** 间接关联 - 关联子键 - 数据库字段名 */
    private String mergeSubFieldSqlName;

    /** 间接关联 - 关联子键值接收键字段 */
    private Field receiveArrKeyField;

    /** 查询操作 */
    private Boolean isSelect;

    /** 新增操作 */
    private Boolean isAdd;

    /** 修改操作 */
    private Boolean isEdit;

    /** 删除操作 */
    private Boolean isDelete;


    public SubRelation(String groupName, Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.subClass = subClass;
        this.relationType = SubTableType.DIRECT;
        this.deleteType = SubDeleteType.NORMAL;
        initOperate(operateTypes);
    }

    public SubRelation(String groupName, Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, SubDeleteType deleteType, SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.subClass = subClass;
        this.relationType = SubTableType.DIRECT;
        this.deleteType = deleteType;
        initOperate(operateTypes);
    }

    public SubRelation(String groupName, Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, Class<? extends BasicMapper<? extends BasisEntity>> mergeClass, SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.subClass = subClass;
        this.mergeClass = mergeClass;
        this.relationType = SubTableType.INDIRECT;
        this.deleteType = SubDeleteType.NORMAL;
        initOperate(operateTypes);
    }

    public SubRelation(String groupName, Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, Class<? extends BasicMapper<? extends BasisEntity>> mergeClass, SubDeleteType deleteType, SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.subClass = subClass;
        this.mergeClass = mergeClass;
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
                    case EX_SEL_OR_ADD_OR_EDIT -> {
                        this.isSelect = Boolean.FALSE;
                        this.isAdd = Boolean.FALSE;
                        this.isEdit = Boolean.FALSE;
                    }
                    case EX_SEL_OR_EDIT -> {
                        this.isSelect = Boolean.FALSE;
                        this.isEdit = Boolean.FALSE;
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
