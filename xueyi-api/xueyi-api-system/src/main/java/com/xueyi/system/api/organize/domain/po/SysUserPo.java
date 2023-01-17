package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.annotation.Excel.Type;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import com.xueyi.common.core.xss.Xss;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 用户 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user", excludeProperty = {"name"})
public class SysUserPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户编码 */
    @Excel(name = "用户编码(*)")
    @Xss(message = "用户编码不能包含脚本字符")
    @NotBlank(message = "用户编码不能为空")
    @Size(max = 64, message = "用户编码长度不能超过64个字符")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 用户账号 */
    @Excel(name = "登录名称")
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String userName;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    @TableField(condition = LIKE)
    @Xss(message = "用户编码不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    protected String nickName;

    /** 用户标识（00超级管理员） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String userType;

    /** 手机号码 */
    @Excel(name = "手机号码")
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    protected String phone;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    protected String email;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    protected String sex;

    /** 用户头像 */
    protected String avatar;

    /** 个人简介 */
    protected String profile;

    /** 密码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String password;

    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    protected String loginIp;

    /** 最后登录时间 */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    protected LocalDateTime loginDate;

}
