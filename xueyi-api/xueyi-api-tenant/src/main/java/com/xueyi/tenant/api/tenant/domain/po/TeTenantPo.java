package com.xueyi.tenant.api.tenant.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 租户服务 | 租户模块 | 租户 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("te_tenant")
public class TeTenantPo extends BaseEntity {

  @Serial private static final long serialVersionUID = 1L;

  /** 策略Id */
  @TableField(updateStrategy = FieldStrategy.NEVER)
  protected Long strategyId;

  /** 名称 */
  @Xss(message = "企业账号不能包含脚本字符")
  @NotBlank(message = "企业账号不能为空")
  @Size(max = 30, message = "企业账号长度不能超过30个字符")
  @TableField(condition = LIKE, updateStrategy = FieldStrategy.NEVER)
  protected String name;

  /** 系统名称 */
  @Xss(message = "系统名称不能包含脚本字符")
  @NotBlank(message = "系统名称不能为空")
  @Size(max = 30, message = "系统名称长度不能超过30个字符")
  protected String systemName;

  /** 租户名称 */
  @Xss(message = "企业名称不能包含脚本字符")
  @NotBlank(message = "企业名称不能为空")
  @Size(max = 30, message = "企业名称长度不能超过30个字符")
  protected String nick;

  /** 租户logo */
  protected String logo;

  /** 账号修改次数 */
  protected Integer nameFrequency;

  /** 超管租户（Y是 N否） */
  protected String isLessor;

  /** 企业自定义域名 */
  @Xss(message = "企业自定义域名不能包含脚本字符")
  @Size(max = 30, message = "企业自定义域名长度不能超过30个字符")
  protected String doMain;

  /** 默认租户（Y是 N否） */
  @TableField(updateStrategy = FieldStrategy.NEVER)
  protected String isDefault;
}
