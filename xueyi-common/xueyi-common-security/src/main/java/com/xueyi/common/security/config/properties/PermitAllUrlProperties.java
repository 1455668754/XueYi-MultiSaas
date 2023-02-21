package com.xueyi.common.security.config.properties;

import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.ReUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.security.annotation.InnerAuth;
import lombok.Getter;
import lombok.Setter;
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

import java.util.*;
import java.util.regex.Pattern;

/**
 * 匿名访问控制
 *
 * @author xueyi
 */
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.ignore.whites")
public class PermitAllUrlProperties implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private static final String[] DEFAULT_IGNORE_URLS = new String[]{"/actuator/**", "/error", "/v3/api-docs"};

    /** 常规全部 */
    @Getter
    @Setter
    private List<String> routine = new ArrayList<>();

    /** 自定义 */
    @Getter
    @Setter
    private MultiValueMap<RequestMethod, String> custom = new LinkedMultiValueMap<>();

    @Override
    public void afterPropertiesSet() {
        routine.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
        RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);
            // 获取类上边的注解
            InnerAuth controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), InnerAuth.class);
            initialize(controller, info);

            // 获取方法上边的注解
            InnerAuth method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), InnerAuth.class);
            initialize(method, info);
        });
    }

    private void initialize(InnerAuth innerAuth, RequestMappingInfo info) {
        Optional.ofNullable(innerAuth).filter(ObjectUtil::isNotNull).filter(InnerAuth::isAnonymous)
                .ifPresent(inner -> {
                            List<String> urls = Objects.requireNonNull(info.getPatternsCondition()).getPatterns().stream().map(url -> ReUtil.replaceAll(url, PATTERN, "*")).toList();
                            if (CollUtil.isEmpty(urls))
                                return;
                            Set<RequestMethod> methods = Objects.requireNonNull(info.getMethodsCondition()).getMethods();
                            if (CollUtil.isNotEmpty(methods))
                                methods.forEach(method -> custom.addAll(method, urls));
                            else
                                routine.addAll(urls);
                        }
                );
    }
}