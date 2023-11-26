package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.model.SysEnterprise;
import com.xueyi.common.core.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 系统服务 | 组织模块 | 企业 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("te_tenant")
public class SysEnterprisePo extends SysEnterprise {

  @Serial private static final long serialVersionUID = 1L;

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
}
