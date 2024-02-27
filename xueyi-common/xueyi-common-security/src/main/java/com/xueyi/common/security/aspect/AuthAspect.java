package com.xueyi.common.security.aspect;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.exception.InnerAuthException;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.annotation.ApiAuth;
import com.xueyi.common.security.annotation.ExternalAuth;
import com.xueyi.common.security.annotation.InnerAuth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 服务调用验证处理
 *
 * @author xueyi
 */
@Slf4j
@Aspect
@Component
public class AuthAspect implements Ordered {

    @Pointcut(value = "@within(com.xueyi.common.security.annotation.InnerAuth) || @annotation(com.xueyi.common.security.annotation.InnerAuth)")
    public void innerAuthCut() {
    }

    /**
     * 内部认证校验
     */
    @SneakyThrows
    @Around(value = "innerAuthCut()")
    public Object innerAuthAround(ProceedingJoinPoint point) {
        InnerAuth innerAuth = getAnnotation(point, InnerAuth.class);
        HttpServletRequest request = ServletUtil.getRequest();
        Assert.notNull(request, "request cannot be null");
        String source = request.getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StrUtil.equals(SecurityConstants.INNER, source)) {
            log.warn("请求地址'{}'，没有内部访问权限", request.getRequestURI());
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        if (!innerAuth.isAnonymous()) {
            // 用户信息验证
            if (ObjectUtil.isNotNull(innerAuth) && innerAuth.isUser()) {
                String enterpriseId = request.getHeader(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode());
                String userId = request.getHeader(SecurityConstants.BaseSecurity.USER_ID.getCode());
                String sourceName = request.getHeader(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
                String accountType = request.getHeader(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
                if ((StrUtil.hasBlank(enterpriseId, userId, sourceName, accountType))) {
                    log.warn("请求地址'{}'，没有设置用户信息", request.getRequestURI());
                    throw new InnerAuthException("没有设置用户信息，不允许访问");
                }
            }
        }
        return point.proceed();
    }

    @Pointcut(value = "@within(com.xueyi.common.security.annotation.AdminAuth) || @annotation(com.xueyi.common.security.annotation.AdminAuth)")
    public void adminAuthCut() {
    }

    /**
     * 管理端认证校验
     */
    @SneakyThrows
    @Around("adminAuthCut()")
    public Object adminAuthAround(ProceedingJoinPoint point) {
        AdminAuth adminAuth = getAnnotation(point, AdminAuth.class);
        HttpServletRequest request = ServletUtil.getRequest();
        Assert.notNull(request, "request cannot be null");
        if (!adminAuth.isAnonymous()) {
            String accountType = request.getHeader(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
            if (StrUtil.notEquals(SecurityConstants.AccountType.ADMIN.getCode(), accountType)) {
                log.warn("请求地址'{}'，没有管理端访问权限", request.getRequestURI());
                throw new InnerAuthException("没有管理端访问权限，不允许访问");
            }
            // 用户信息验证
            if (ObjectUtil.isNotNull(adminAuth) && adminAuth.isUser()) {
                String enterpriseId = request.getHeader(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode());
                String userId = request.getHeader(SecurityConstants.BaseSecurity.USER_ID.getCode());
                String sourceName = request.getHeader(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
                if ((StrUtil.hasBlank(enterpriseId, userId, sourceName, accountType))) {
                    log.warn("请求地址'{}'，没有设置用户信息", request.getRequestURI());
                    throw new InnerAuthException("没有设置用户信息，不允许访问");
                }
            }
        }
        return point.proceed();
    }

    @Pointcut(value = "@within(com.xueyi.common.security.annotation.ExternalAuth) || @annotation(com.xueyi.common.security.annotation.ExternalAuth)")
    public void externalAuthCut() {
    }

    /**
     * 外系统端认证校验
     */
    @SneakyThrows
    @Around("externalAuthCut()")
    public Object externalAuthAround(ProceedingJoinPoint point) {
        ExternalAuth externalAuth = getAnnotation(point, ExternalAuth.class);
        HttpServletRequest request = ServletUtil.getRequest();
        Assert.notNull(request, "request cannot be null");
        if (!externalAuth.isAnonymous()) {
            String accountType = request.getHeader(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
            if (StrUtil.notEquals(SecurityConstants.AccountType.EXTERNAL.getCode(), accountType)) {
                log.warn("请求地址'{}'，没有外系统端访问权限", request.getRequestURI());
                throw new InnerAuthException("没有外系统端访问权限，不允许访问");
            }
        }
        return point.proceed();
    }

    @Pointcut(value = "@within(com.xueyi.common.security.annotation.ApiAuth) || @annotation(com.xueyi.common.security.annotation.ApiAuth)")
    public void apiAuthCut() {
    }

    /**
     * Api端认证校验
     */
    @SneakyThrows
    @Around("apiAuthCut()")
    public Object apiAuthAround(ProceedingJoinPoint point) {
        ApiAuth apiAuth = getAnnotation(point, ApiAuth.class);
        HttpServletRequest request = ServletUtil.getRequest();
        Assert.notNull(request, "request cannot be null");
        return point.proceed();
    }

    /**
     * 通过连接点对象获取其方法或类上的注解
     *
     * @param point     连接点
     * @param authClass 注解类型
     * @return 注解
     */
    private <T extends Annotation> T getAnnotation(ProceedingJoinPoint point, Class<T> authClass) {
        T auth = null;
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (ObjectUtil.isNotNull(method)) {
            if (method.isAnnotationPresent(authClass)) {
                auth = method.getAnnotation(authClass);
            }
            if (ObjectUtil.isNull(auth) && method.getDeclaringClass().isAnnotationPresent(authClass)) {
                auth = method.getDeclaringClass().getAnnotation(authClass);
            }
        }
        return auth;
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
