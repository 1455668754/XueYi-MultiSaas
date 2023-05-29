package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TTreeEntity;
import com.xueyi.common.core.xss.Xss;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 系统服务 | 组织模块 | 部门 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDeptPo extends TTreeEntity<SysDeptDto> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门编码 */
    @Xss(message = "用户账号不能包含脚本字符")
    @Size(max = 30, message = "部门编码长度不能超过20个字符")
    protected String code;

    /** 名称 */
    @TableField(condition = LIKE)
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    protected String name;

    /** 负责人 */
    protected String leader;

    /** 联系电话 */
    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    protected String phone;

    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    protected String email;

}
