package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.constant.error.UtilErrorConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.domain.SqlField;
import com.xueyi.common.web.entity.domain.SubRelation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MergeUtil {

    /**
     * 子数据映射关联
     *
     * @param dtoList         数据对象集合
     * @param subRelationList 子类关联对象集合
     * @param DClass          数据对象Class
     */
    public static <D> List<D> subRelationBuild(List<D> dtoList, List<SubRelation> subRelationList, Class<D> DClass) {
        if (CollUtil.isEmpty(subRelationList))
            return dtoList;
        for (SubRelation subRelation : subRelationList) {
            if (ObjectUtil.hasNull(subRelation.getMainKeyField(), subRelation.getSubKeyField(), subRelation.getReceiveKeyField()))
                initRelationField(subRelation, DClass);
            switch (subRelation.getRelationType()) {
                case DIRECT:
                    directRelationBuild(null, dtoList, subRelation, DClass, OperateConstants.DataRow.COLLECTION);
                    break;
                case INDIRECT:
                    indirectRelationBuild(null, dtoList, subRelation, DClass, OperateConstants.DataRow.COLLECTION);
                    break;
            }
        }
        return dtoList;
    }

    /**
     * 直接关联
     *
     * @param dto         数据对象
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    private static <D> void directRelationBuild(D dto, List<D> dtoList, SubRelation subRelation, Class<D> DClass, OperateConstants.DataRow dataRow) {
        switch (dataRow){
            case SINGLE:
                if(ObjectUtil.isNull(dto))
                    return;
                break;
            case COLLECTION:
                if(CollUtil.isEmpty(dtoList))
                    return;
                TableField tableField = subRelation.getSubKeyField().getAnnotation(TableField.class);

                Set<Object> findInSet = dtoList.stream().map().collect(Collectors.toSet());
                SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, StrUtil.isNotEmpty(tableField.value()) ? tableField.value() : StrUtil.toUnderlineCase(subRelation.getSubKeyField().getName()), null);

                break;
        }
    }

    /**
     * 间接关联
     *
     * @param dto         数据对象
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    private static <D> void indirectRelationBuild(D dto, List<D> dtoList, SubRelation subRelation, Class<D> DClass, OperateConstants.DataRow dataRow) {

    }

    /**
     * 初始化数据字段关系
     *
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    private static <D> void initRelationField(SubRelation subRelation, Class<D> DClass) {
        // select subKeyField
        Class<? extends BaseEntity> subPClass = SpringUtil.getBean(subRelation.getSubClass()).getPClass();
        Field[] subFields = ReflectUtil.getFields(subPClass);
        subRelation.setSubKeyField(Arrays.stream(subFields).filter(field -> {
            com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
            return relation != null && StrUtil.equals(relation.groupName(), subRelation.getGroupName()) && relation.keyType().isSubKey();
        }).findFirst().orElse(null));
        if (ObjectUtil.isNull(subRelation.getSubKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.SUB_KEY.getInfo()));
        Field[] mainFields = ReflectUtil.getFields(DClass);
        for (Field field : mainFields) {
            com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
            if (relation != null) {
                if (StrUtil.notEquals(relation.groupName(), subRelation.getGroupName()))
                    continue;
                if (relation.keyType().isMainKey() && ObjectUtil.isNull(subRelation.getMainKeyField()))
                    subRelation.setMainKeyField(field);
                else if (relation.keyType().isReceiveKey() && ObjectUtil.isNull(subRelation.getReceiveKeyField()))
                    subRelation.setReceiveKeyField(field);
            }
        }
        // if null then assignment idKey
        if (ObjectUtil.isNull(subRelation.getMainKeyField()))
            subRelation.setMainKeyField(Arrays.stream(mainFields).filter(field -> field.getAnnotation(TableId.class) != null).findFirst().orElse(null));
        // if it has any null throws exception
        if (ObjectUtil.isNull(subRelation.getMainKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.MAIN_KEY.getInfo()));
        if (ObjectUtil.isNull(subRelation.getReceiveKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.RECEIVE_KEY.getInfo()));
    }
}