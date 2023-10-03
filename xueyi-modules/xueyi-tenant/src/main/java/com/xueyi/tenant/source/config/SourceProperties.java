package com.xueyi.tenant.source.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源管理配置
 *
 * @author xueyi
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "source-config")
public class SourceProperties {

    /** 租户源表 */
    private String[] slaveTable;

}