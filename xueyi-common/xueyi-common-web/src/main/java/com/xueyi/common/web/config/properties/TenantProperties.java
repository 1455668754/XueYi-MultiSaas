package com.xueyi.common.web.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 租户表控制配置
 *
 * @author xueyi
 */
@Configuration
@ConfigurationProperties(prefix = "xueyi.tenant")
public class TenantProperties implements BeanPostProcessor {

    /** 公共表 */
    @Getter
    private static Boolean ignoreTenant = Boolean.FALSE;

    /** 公共表 */
    @Getter
    private static String[] commonTable;

    /** 非租户表 */
    @Getter
    private static String[] excludeTable;

    public void setIgnoreTenant(Boolean ignoreTenant) {
        TenantProperties.ignoreTenant = ignoreTenant;
    }

    public void setCommonTable(String[] commonTable) {
        TenantProperties.commonTable = commonTable;
    }

    public void setExcludeTable(String[] excludeTable) {
        TenantProperties.excludeTable = excludeTable;
    }

}