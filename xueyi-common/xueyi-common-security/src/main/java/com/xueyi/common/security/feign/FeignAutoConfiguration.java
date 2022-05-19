package com.xueyi.common.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 配置注册
 *
 * @author xueyi
 **/
@Configuration
public class FeignAutoConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
