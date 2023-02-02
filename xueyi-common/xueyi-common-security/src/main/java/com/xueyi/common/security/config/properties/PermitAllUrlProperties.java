package com.xueyi.common.security.config.properties;

import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xueyi.common.security.annotation.InnerAuth;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 设置InnerAuth注解允许匿名访问的url
 *
 * @author xueyi
 */
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlProperties implements InitializingBean {
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private static final String[] DEFAULT_IGNORE_URLS = new String[]{"/actuator/**", "/error", "/v3/api-docs"};

    @Getter
    @Setter
    private List<String> urls = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        urls.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
        RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 获取方法上边的注解 替代path variable 为 *
            InnerAuth method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), InnerAuth.class);
            Optional.ofNullable(method).filter(InnerAuth::isAnonymous).ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
                    .getPatternValues().forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));

            // 获取类上边的注解, 替代path variable 为 *
            InnerAuth controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), InnerAuth.class);
            Optional.ofNullable(controller).filter(InnerAuth::isAnonymous).ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
                    .getPatternValues().forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
        });
    }

}
