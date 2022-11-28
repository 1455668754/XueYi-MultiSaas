package com.xueyi.common.datasource.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 主数据源配置
 *
 * @author xueyi
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
public class SourceProperties {

    /** 数据源驱动 */
    private String driverClassName;

    /** 数据源路径 */
    private String url;

    /** 数据源账号 */
    private String username;

    /** 数据源密码 */
    private String password;

}
