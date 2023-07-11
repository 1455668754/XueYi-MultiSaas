package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import com.xueyi.common.core.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 系统服务 | 组织模块 | 岗位 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
public class SysPostPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门Id */
    @NotNull(message = "归属部门不能为空")
    protected Long deptId;

    /** 岗位编码 */
    @Excel(name = "岗位编码(*)")
    @Xss(message = "岗位编码不能包含脚本字符")
    @Size(max = 64, message = "岗位编码长度不能超过64个字符")
    protected String code;

    /** 名称 */
    @TableField(condition = LIKE)
    @Xss(message = "岗位名称不能包含脚本字符")
    @NotBlank(message = "岗位名称不能为空")
    @Size(max = 50, message = "岗位名称长度不能超过50个字符")
    protected String name;

}
