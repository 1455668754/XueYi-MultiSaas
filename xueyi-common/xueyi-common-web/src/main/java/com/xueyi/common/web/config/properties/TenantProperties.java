package com.xueyi.common.web.config.properties;

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
    private static String[] commonTable;

    /** 非租户表 */
    private static String[] excludeTable;

    public static String[] getCommonTable() {
        return commonTable;
    }

    public void setCommonTable(String[] commonTable) {
        TenantProperties.commonTable = commonTable;
    }

    public static String[] getExcludeTable() {
        return excludeTable;
    }

    public void setExcludeTable(String[] excludeTable) {
        TenantProperties.excludeTable = excludeTable;
    }

}