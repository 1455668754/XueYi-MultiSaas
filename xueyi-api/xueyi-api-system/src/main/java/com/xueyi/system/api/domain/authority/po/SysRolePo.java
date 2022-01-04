package com.xueyi.system.api.domain.authority.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.TBaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色 持久化对象
 *
 * @author xueyi
 */
public class SysRolePo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 岗位编码 */
    @TableField("code")
    private String code;

    /** 角色名称 */
    @Excel(name = "角色名称")
    @TableField("name")
    private String name;

    /** 角色权限 */
    @Excel(name = "角色权限")
    @TableField("role_key")
    private String roleKey;

    /** 数据范围（1：全部数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限 5：本岗位数据权限 6：仅本人数据权限） */
    @Excel(name = "数据范围", readConverterExp = "1=全部数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=本岗位数据权限,6=仅本人数据权限")
    @TableField("data_scope")
    private String dataScope;

    /** 默认菜单（Y是 N否） */
    @TableField("is_default")
    private String isDefault;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
