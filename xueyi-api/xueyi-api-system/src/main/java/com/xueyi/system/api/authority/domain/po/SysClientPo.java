package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 客户端 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_oauth_client", excludeProperty = {"name"})
public class SysClientPo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 客户端ID */
    @NotBlank(message = "client_id 不能为空")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String clientId;

    /** 客户端密钥 */
    @NotBlank(message = "client_secret 不能为空")
    private String clientSecret;

    /** 资源ID */
    private String resourceIds;

    /** 作用域 */
    @NotBlank(message = "scope 不能为空")
    private String scope;

    /** 授权方式（A,B,C） */
    private String authorizedGrantTypes;

    /** 回调地址 */
    private String webServerRedirectUri;

    /** 权限 */
    private String authorities;

    /** 请求令牌有效时间 */
    private Integer accessTokenValidity;

    /** 刷新令牌有效时间 */
    private Integer refreshTokenValidity;

    /** 扩展信息 */
    private String additionalInformation;

    /** 是否自动放行 */
    private String autoApprove;
}
