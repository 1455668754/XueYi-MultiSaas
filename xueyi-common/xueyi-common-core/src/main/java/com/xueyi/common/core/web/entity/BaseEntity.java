package com.xueyi.common.core.web.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * Base 基类
 *
 * @author xueyi
 */
public class BaseEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** Id */
    @TableId("id")
    private Long id;

    /** 名称 */
    @TableField(value = "name", condition = LIKE)
    private String name;

    /** 状态（0 启用 1 禁用） */
    @TableField("status")
    private String status;

    /** 显示顺序 */
    @TableField("sort")
    protected Integer sort;

    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 创建者Id */
    @TableField(value = "create_by", updateStrategy = FieldStrategy.NEVER)
    private Long createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    /** 更新者Id */
    @TableField(value = "update_by", insertStrategy = FieldStrategy.NEVER)
    private Long updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;

    /** 删除标志 */
    @TableField(value = "del_flag", select = false)
    @TableLogic(value = "0", delval = "1")
    private Long delFlag;

    /** 雪花Id */
    @TableField(exist = false)
    private Long snowflakeId;

    /** 创建者 */
    @TableField(exist = false)
    private String createName;

    /** 创建者 */
    @TableField(exist = false)
    private String updateName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    public Long getSnowflakeId() {
        return snowflakeId;
    }

    public void setSnowflakeId(Long snowflakeId) {
        this.snowflakeId = snowflakeId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

}