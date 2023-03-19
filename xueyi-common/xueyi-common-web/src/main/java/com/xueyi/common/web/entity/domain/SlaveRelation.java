package com.xueyi.common.web.entity.domain;

import com.xueyi.common.core.constant.basic.OperateConstants.SubDeleteType;
import com.xueyi.common.core.constant.basic.OperateConstants.SubOperateLimit;
import com.xueyi.common.core.constant.basic.OperateConstants.SubTableType;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.common.web.entity.mapper.BasicMapper;
import com.xueyi.common.web.entity.service.IBaseService;
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

    /** 从数据 - Service接口class */
    private Class<? extends IBaseService> slaveServiceClass;

    /** 从数据 - Manager接口class */
    private Class<? extends IBaseManager> slaveManagerClass;

    /** 主数据对象 - 主键字段 */
    private Field mainIdField;

    /** 主数据对象 - 关联键字段 */
    private Field mainField;

    /** 从数据对象 - 主键字段 */
    private Field slaveIdField;

    /** 从数据对象 - 主键字段 - 数据库字段名 */
    private String slaveIdFieldSqlName;

    /** 从数据对象 - 关联键字段 */
    private Field slaveField;

    /** 从数据对象 - 关联键字段 - 数据库字段名 */
    private String slaveFieldSqlName;

    /** 主数据对象 - 关联数据接收字段 */
    private Field receiveField;

    /** 间接关联 - 关联Mapper接口class */
    private Class<? extends BasicMapper> mergeClass;

    /** 中间数据对象 - 间接关联 - 关联数据对象class */
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

    /** 列表查询操作 */
    private Boolean isList;

    /** 单个查询操作 */
    private Boolean isSingle;

    /** 联动操作控制 */
    private LinkageOperate linkageOperate;

    public SlaveRelation(String groupName,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, null, slaveManagerClass, null, null, SubTableType.DIRECT, SubDeleteType.NORMAL, null, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseService<? extends BaseEntity, ? extends BaseEntity>> slaveServiceClass,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         LinkageOperate linkageOperate,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, slaveServiceClass, slaveManagerClass, null, null, SubTableType.DIRECT, SubDeleteType.NORMAL, linkageOperate, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         SubDeleteType deleteType,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, null, slaveManagerClass, null, null, SubTableType.DIRECT, deleteType, null, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseService<? extends BaseEntity, ? extends BaseEntity>> slaveServiceClass,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         SubDeleteType deleteType,
                         LinkageOperate linkageOperate,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, slaveServiceClass, slaveManagerClass, null, null, SubTableType.DIRECT, deleteType, linkageOperate, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, null, slaveManagerClass, mergeClass, mergePoClass, SubTableType.INDIRECT, SubDeleteType.NORMAL, null, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseService<? extends BaseEntity, ? extends BaseEntity>> slaveServiceClass,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         LinkageOperate linkageOperate,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, slaveServiceClass, slaveManagerClass, mergeClass, mergePoClass, SubTableType.INDIRECT, SubDeleteType.NORMAL, linkageOperate, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, null, null, mergeClass, mergePoClass, SubTableType.INDIRECT, SubDeleteType.NORMAL, null, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubDeleteType deleteType,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, null, slaveManagerClass, mergeClass, mergePoClass, SubTableType.INDIRECT, deleteType, null, operateTypes);
    }

    public SlaveRelation(String groupName,
                         Class<? extends IBaseService<? extends BaseEntity, ? extends BaseEntity>> slaveServiceClass,
                         Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                         Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                         Class<? extends BasisEntity> mergePoClass,
                         SubDeleteType deleteType,
                         LinkageOperate linkageOperate,
                         SubOperateLimit... operateTypes) {
        initOperate(groupName, slaveServiceClass, slaveManagerClass, mergeClass, mergePoClass, SubTableType.INDIRECT, deleteType, linkageOperate, operateTypes);
    }

    /**
     * 初始化操作
     *
     * @param groupName         分组名称 | 必须全局唯一
     * @param slaveServiceClass 从数据 - Service接口class
     * @param slaveManagerClass 从数据 - Manager接口class
     * @param mergeClass        间接关联 - 关联Mapper接口class
     * @param mergePoClass      中间数据对象 - 间接关联 - 关联数据对象class
     * @param relationType      关联类型
     * @param deleteType        删除类型
     * @param linkageOperate    联动操作控制
     * @param operateTypes      操作控制类型
     */
    private void initOperate(String groupName,
                             Class<? extends IBaseService<? extends BaseEntity, ? extends BaseEntity>> slaveServiceClass,
                             Class<? extends IBaseManager<? extends BaseEntity, ? extends BaseEntity>> slaveManagerClass,
                             Class<? extends BasicMapper<? extends BasisEntity>> mergeClass,
                             Class<? extends BasisEntity> mergePoClass,
                             SubTableType relationType,
                             SubDeleteType deleteType,
                             LinkageOperate linkageOperate,
                             SubOperateLimit... operateTypes) {
        this.groupName = groupName;
        this.slaveServiceClass = slaveServiceClass;
        this.slaveManagerClass = slaveManagerClass;
        this.mergeClass = mergeClass;
        this.mergePoClass = mergePoClass;
        this.relationType = relationType;
        this.deleteType = deleteType;
        this.linkageOperate = ObjectUtil.isNotNull(slaveServiceClass) && ObjectUtil.isNotNull(linkageOperate)
                ? linkageOperate
                : new LinkageOperate(ObjectUtil.isNotNull(slaveServiceClass) ? Boolean.TRUE : Boolean.FALSE);

        if (ArrayUtil.isNotEmpty(operateTypes)) {
            for (SubOperateLimit operateType : operateTypes) {
                switch (operateType) {
                    case EX_SEL -> this.isSelect = Boolean.FALSE;
                    case EX_LIST_SEL, ONLY_SINGLE_SEL -> this.isList = Boolean.FALSE;
                    case EX_SINGLE_SEL, ONLY_LIST_SEL -> this.isSingle = Boolean.FALSE;
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

        if (ObjectUtil.isNull(this.isSelect)) {
            if (ObjectUtil.isNull(this.isList))
                this.isList = Boolean.TRUE;
            if (ObjectUtil.isNull(this.isSingle))
                this.isSingle = Boolean.TRUE;

            if (this.isList && this.isSingle)
                this.isSelect = Boolean.TRUE;
            else
                this.isSelect = Boolean.FALSE;
        } else if (ObjectUtil.equals(this.isSelect, Boolean.FALSE)) {
            this.isList = Boolean.FALSE;
            this.isSingle = Boolean.FALSE;
        }

        if (ObjectUtil.isNull(this.isAdd))
            this.isAdd = Boolean.TRUE;
        if (ObjectUtil.isNull(this.isEdit))
            this.isEdit = Boolean.TRUE;
        if (ObjectUtil.isNull(this.isDelete))
            this.isDelete = Boolean.TRUE;
    }

    /**
     * 联动操作控制
     */
    @Data
    @NoArgsConstructor
    public static class LinkageOperate {

        /** 查询操作 */
        private Boolean isSelect = true;

        /** 新增操作 */
        private Boolean isAdd = true;

        /** 修改操作 */
        private Boolean isEdit = true;

        /** 删除操作 */
        private Boolean isDelete = true;

        public LinkageOperate(Boolean operate) {
            this.isSelect = operate;
            this.isAdd = operate;
            this.isEdit = operate;
            this.isDelete = operate;
        }

        public LinkageOperate(Boolean isSelect, Boolean isAdd, Boolean isEdit, Boolean isDelete) {
            this.isSelect = isSelect;
            this.isAdd = isAdd;
            this.isEdit = isEdit;
            this.isDelete = isDelete;
        }
    }
}
