package com.xueyi.system.api.domain.organize.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.TBaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 岗位 持久化对象
 *
 * @author xueyi
 */
public class SysPostPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 部门Id */
    @TableField("dept_id")
    private Long deptId;

    /** 岗位编码 */
    @Excel(name = "岗位编码(*)")
    @TableField("code")
    private String code;

    /** 岗位名称 */
    @Excel(name = "岗位名称(*)")
    @TableField("name")
    private String name;

    /** 默认岗位（Y是 N否） */
    @TableField("is_default")
    private String isDefault;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
