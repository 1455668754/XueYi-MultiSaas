package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 系统服务 | 权限模块 | 角色 持久化对象
 *
 * @author xueyi
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRolePo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 角色编码 */
    @Excel(name = "角色编码")
    protected String code;

    /** 名称 */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    @TableField(condition = LIKE)
    protected String name;

    /** 角色权限字符串 */
    @Excel(name = "角色权限字符串")
    @Size(max = 100, message = "权限字符长度不能超过100个字符")
    protected String roleKey;

    /** 数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5本岗位数据权限  6仅本人数据权限） */
    @Excel(name = "数据范围", readConverterExp = "1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=本岗位数据权限,6=仅本人数据权限")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String dataScope;

}
