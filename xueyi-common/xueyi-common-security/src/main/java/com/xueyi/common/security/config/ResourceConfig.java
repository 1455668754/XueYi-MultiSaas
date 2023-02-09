package com.xueyi.common.security.config;

import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.auth.AuthService;
import org.springframework.context.annotation.Bean;

/**
 * 资源服务配置
 *
 * @author xueyi
 */
public class ResourceConfig {

    /**
     * 权限验证
     */
    @Bean("ss")
    public AuthService authService() {
        return new AuthService();
    }

    /**
     * 权限标识常量
     */
    @Bean("Auth")
    public Auth auth() {
        return new Auth();
    }

}
