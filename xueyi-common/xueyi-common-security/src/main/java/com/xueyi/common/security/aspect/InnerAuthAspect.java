package com.xueyi.common.security.aspect;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.exception.InnerAuthException;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.annotation.InnerAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 内部服务调用验证处理
 *
 * @author xueyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        String source = ServletUtil.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StrUtil.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String enterpriseId = ServletUtil.getRequest()
                .getHeader(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode());
        String userId = ServletUtil.getRequest().getHeader(SecurityConstants.BaseSecurity.USER_ID.getCode());
        String sourceName = ServletUtil.getRequest().getHeader(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
        String accountType = ServletUtil.getRequest().getHeader(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
        // 用户信息验证
        if (innerAuth.isUser() && (StrUtil.hasBlank(enterpriseId, userId, sourceName, accountType))) {
            throw new InnerAuthException("没有设置用户信息，不允许访问");
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
