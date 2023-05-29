package com.xueyi.system.api.application.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.xueyi.common.core.constant.basic.EntityConstants.SORT;

/**
 * 系统服务 | 应用模块 | 应用 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_application", excludeProperty = { SORT })
public class SysApplicationPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** appId */
    protected String appId;

    /** 应用密钥 */
    protected String appSecret;

    /** 应用令牌 */
    protected String appToken;

    /** 应用Aes */
    protected String appAes;

    /** 应用补充键1 */
    protected String appKeyOne;

    /** 应用补充键2 */
    protected String appKeyTwo;

    /** 应用补充键3 */
    protected String appKeyThr;

    /** 应用补充键4 */
    protected String appKeyFou;

    /** 应用补充键5 */
    protected String appKeyFiv;

    /** 应用补充键6 */
    protected String appKeySix;

    /** 消息应用类型（0微信公众号 1微信小程序 2短信 3邮件 4钉钉） */
    protected String type;

    /** 租户Id */
    @JsonIgnore
    protected Long tenantId;
}