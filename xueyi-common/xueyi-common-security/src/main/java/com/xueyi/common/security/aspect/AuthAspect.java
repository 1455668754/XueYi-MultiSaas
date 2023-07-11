package com.xueyi.common.security.aspect;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.exception.InnerAuthException;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.annotation.InnerAuth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 服务调用验证处理
 *
 * @author xueyi
 */
@Slf4j
@Aspect
@Component
public class AuthAspect implements Ordered {

    /**
     * 内部认证校验
     */
    @SneakyThrows
    @Around("@within(innerAuth) || @annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        HttpServletRequest request = ServletUtil.getRequest();
        Assert.notNull(request, "request cannot be null");
        String source = request.getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StrUtil.equals(SecurityConstants.INNER, source)) {
            log.warn("请求地址'{}'，没有内部访问权限", request.getRequestURI());
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        // 用户信息验证
        if (innerAuth.isUser()) {
            String enterpriseId = request.getHeader(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode());
            String userId = request.getHeader(SecurityConstants.BaseSecurity.USER_ID.getCode());
            String sourceName = request.getHeader(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
            String accountType = request.getHeader(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
            if ((StrUtil.hasBlank(enterpriseId, userId, sourceName, accountType))) {
                log.warn("请求地址'{}'，没有设置用户信息", request.getRequestURI());
                throw new InnerAuthException("没有设置用户信息，不允许访问");
            }
        }
        return point.proceed();
    }

    /**
     * 管理端认证校验
     */
    @SneakyThrows
    @Around("@within(adminAuth) || @annotation(adminAuth)")
    public Object innerAround(ProceedingJoinPoint point, AdminAuth adminAuth) {
        HttpServletRequest request = ServletUtil.getRequest();
        Assert.notNull(request, "request cannot be null");
        String accountType = request.getHeader(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
        if (StrUtil.notEquals(SecurityConstants.AccountType.ADMIN.getCode(), accountType)) {
            log.warn("请求地址'{}'，没有管理端访问权限", request.getRequestURI());
            throw new InnerAuthException("没有管理端访问权限，不允许访问");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
