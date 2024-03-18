package com.xueyi.common.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger 配置属性
 *
 * @author Vondser
 */
@Data
@ConfigurationProperties(prefix = "springdoc")
public class SwaggerProperties {

    /** 文档基本信息 */
    private InfoProperties info = new InfoProperties();

    /**
     * 文档的基础属性信息
     * 为了 springboot 自动生产配置提示信息，所以这里复制一个类出来
     */
    @Data
    public static class InfoProperties {

        /** 标题 */
        private String title;

        /** 描述 */
        private String description;

        /** 联系人信息 */
        private Contact contact;

        /** 许可证 */
        private License license;

        /** 版本 */
        private String version;

        /** 服务条款URL **/
        private String termsOfService = "";

    }

    @Data
    public static class Contact {

        /** 联系人 **/
        private String name = "";

        /** 联系人url **/
        private String url = "";

        /** 联系人email **/
        private String email = "";

    }

    @Data
    public static class License {

        /** 联系人 **/
        private String name = "";

        /** 联系人url **/
        private String url = "";

    }
}