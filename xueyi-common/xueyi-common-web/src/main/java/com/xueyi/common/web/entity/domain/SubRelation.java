package com.xueyi.common.web.entity.domain;

import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.common.web.entity.mapper.BaseMapper;
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
    private OperateConstants.SubTableType relationType;

    /** 删除类型 */
    private OperateConstants.SubDeleteType deleteType;

    /** 主关联键字段 */
    private Field mainKeyField;

    /** 子关联键字段 */
    private Field subKeyField;

    /** 值接收键字段 */
    private Field receiveKeyField;

    public SubRelation(Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, String groupName) {
        this.subClass = subClass;
        this.groupName = groupName;
        this.relationType = OperateConstants.SubTableType.DIRECT;
        this.deleteType = OperateConstants.SubDeleteType.NORMAL;
    }

    public SubRelation(Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, String groupName, OperateConstants.SubTableType relationType) {
        this.subClass = subClass;
        this.groupName = groupName;
        this.relationType = relationType;
        this.deleteType = OperateConstants.SubDeleteType.NORMAL;
    }

    public SubRelation(Class<? extends BaseManagerImpl<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity, ? extends BaseMapper<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>, ? extends BaseConverter<? extends BaseEntity, ? extends BaseEntity, ? extends BaseEntity>>> subClass, String groupName, OperateConstants.SubTableType relationType, OperateConstants.SubDeleteType deleteType) {
        this.subClass = subClass;
        this.groupName = groupName;
        this.relationType = relationType;
        this.deleteType = deleteType;
    }
}
