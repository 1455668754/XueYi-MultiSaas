package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TSubTreeEntity;
import com.xueyi.common.core.xss.Xss;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;


/**
 * 部门 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDeptPo extends TSubTreeEntity<SysDeptDto, SysPostDto> {

    private static final long serialVersionUID = 1L;

    /** 部门编码 */
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "部门编码不能为空")
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
