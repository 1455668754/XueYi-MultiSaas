package com.xueyi.common.security.config.properties;

import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.ReUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.annotation.ApiAuth;
import com.xueyi.common.security.annotation.ExternalAuth;
import com.xueyi.common.security.annotation.InnerAuth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 匿名访问控制
 *
 * @author xueyi
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.ignore.whites")
public class PermitAllUrlProperties implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private static final String[] DEFAULT_IGNORE_URLS = new String[]{"/actuator/**",
            "/error", "/v3/api-docs", "/v3/api-docs/*", "/doc.html", "/webjars/**"};

    /** 常规全部 */
    private List<String> routine = new ArrayList<>();

    /** 自定义 */
    private MultiValueMap<RequestMethod, String> custom = new LinkedMultiValueMap<>();

    @Override
    public void afterPropertiesSet() {
        routine.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
        RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);
            // 内部认证
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), InnerAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(InnerAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), InnerAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(InnerAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));

            // 外系统端认证
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), ExternalAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(ExternalAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ExternalAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(ExternalAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));

            // 对外Api认证
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), ApiAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(ApiAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(ApiAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));

            // 管理端认证
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AdminAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(AdminAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));
            Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AdminAuth.class))
                    .filter(ObjectUtil::isNotNull).filter(AdminAuth::isAnonymous)
                    .ifPresent(inner -> anonymousFilter(info));
        });
    }

    private void anonymousFilter(RequestMappingInfo info) {
        List<String> urls = Objects.requireNonNull(info.getPatternsCondition()).getPatterns().stream().map(url -> ReUtil.replaceAll(url, PATTERN, "*")).toList();
        if (CollUtil.isEmpty(urls))
            return;
        Set<RequestMethod> methods = Objects.requireNonNull(info.getMethodsCondition()).getMethods();
        if (CollUtil.isNotEmpty(methods)) {
            methods.forEach(method -> custom.addAll(method, urls));
        } else {
            routine.addAll(urls);
        }
    }
}