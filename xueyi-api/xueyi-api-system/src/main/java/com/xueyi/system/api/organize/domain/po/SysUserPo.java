package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.annotation.Excel.Type;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import com.xueyi.common.core.xss.Xss;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 用户 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user", excludeProperty = {"name"})
public class SysUserPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户编码 */
    @Excel(name = "用户编码(*)")
    @Xss(message = "用户编码不能包含脚本字符")
    @NotBlank(message = "用户编码不能为空")
    @Size(max = 64, message = "用户编码长度不能超过64个字符")
    @TableField(value = "code", updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 用户账号 */
    @Excel(name = "登录名称")
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    @TableField(value = "user_name", updateStrategy = FieldStrategy.NEVER)
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    @TableField("nick_name")
    @Xss(message = "用户编码不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    /** 用户标识（00超级管理员） */
    @TableField(value = "user_type", updateStrategy = FieldStrategy.NEVER)
    private String userType;

    /** 手机号码 */
    @Excel(name = "手机号码")
    @TableField("phone")
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    private String phone;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @TableField("email")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @TableField("sex")
    private String sex;

    /** 用户头像 */
    @TableField("avatar")
    private String avatar;

    /** 个人简介 */
    @TableField("profile")
    private String profile;

    /** 密码 */
    @TableField(value = "password", updateStrategy = FieldStrategy.NEVER)
    private String password;

    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    @TableField("login_ip")
    private String loginIp;

    /** 最后登录时间 */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @TableField("login_date")
    private LocalDateTime loginDate;

}
