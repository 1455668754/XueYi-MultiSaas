package com.xueyi.common.web.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.github.pagehelper.PageInterceptor;
import com.xueyi.common.datascope.interceptor.XueYiDataScopeInterceptor;
import com.xueyi.common.web.handler.XueYiTenantLineHandler;
import com.xueyi.common.web.injector.CustomizedSqlInjector;
import com.xueyi.common.web.interceptor.XueYiTenantLineInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MP配置
 *
 * @author xueyi
 */
@Configuration
@MapperScan("com.xueyi.**.mapper")
public class XueYiMyBatisPlusConfig {

    /**
     * PageHelper分页配置
     */
    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    /**
     * 多租户配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加数据权限插件
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
        XueYiDataScopeInterceptor dataScopeAspect = new XueYiDataScopeInterceptor();
        // 添加自定义的数据权限处理器
        dataPermissionInterceptor.setDataPermissionHandler(dataScopeAspect);
        interceptor.addInnerInterceptor(dataPermissionInterceptor);

        interceptor.addInnerInterceptor(new XueYiTenantLineInnerInterceptor(new XueYiTenantLineHandler()));
        return interceptor;
    }

    /**
     * 批量新增|修改
     */
    @Bean
    public CustomizedSqlInjector customizedSqlInjector() {
        return new CustomizedSqlInjector();
    }
}
