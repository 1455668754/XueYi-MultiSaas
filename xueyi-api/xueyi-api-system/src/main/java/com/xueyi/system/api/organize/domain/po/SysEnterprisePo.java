package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.xss.Xss;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 企业 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("te_tenant")
public class SysEnterprisePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    protected Long strategyId;

    /** 名称 */
    @TableField(condition = LIKE)
    @Xss(message = "企业账号不能包含脚本字符")
    @NotBlank(message = "企业账号不能为空")
    @Size(max = 30, message = "企业账号长度不能超过30个字符")
    protected String name;

    /** 系统名称 */
    @Xss(message = "系统名称不能包含脚本字符")
    @NotBlank(message = "系统名称不能为空")
    @Size(max = 30, message = "系统名称长度不能超过30个字符")
    protected String systemName;

    /** 企业名称 */
    @Xss(message = "企业名称不能包含脚本字符")
    @NotBlank(message = "企业名称不能为空")
    @Size(max = 30, message = "企业名称长度不能超过30个字符")
    protected String nick;

    /** 企业logo */
    protected String logo;

    /** 超管租户（Y是 N否） */
    protected String isLessor;

    /** 企业账号修改次数 */
    protected Long nameFrequency;

    /** 默认企业（Y是 N否） */
    protected String isDefault;

}
