package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * Base 基类
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 名称 */
    @TableField(condition = LIKE)
    protected String name;

    /** 状态（0 启用 1 禁用） */
    protected String status;

    /** 显示顺序 */
    @OrderBy(asc = true, sort = 10)
    protected Integer sort;

    /** 备注 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String remark;

    /** 创建者Id */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    protected Long createBy;

    /** 创建时间 */
    @OrderBy(sort = 20)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    protected LocalDateTime createTime;

    /** 更新者Id */
    @TableField(fill = FieldFill.UPDATE, insertStrategy = FieldStrategy.NEVER)
    protected Long updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    protected LocalDateTime updateTime;

    /** 删除标志 */
    @TableField(select = false)
    @TableLogic(value = "0", delval = "1")
    protected Long delFlag;

    /** 创建者 */
    @TableField(exist = false)
    protected String createName;

    /** 创建者 */
    @TableField(exist = false)
    protected String updateName;

}