package com.xueyi.gateway.filter;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants.AccountType;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 *
 * @author xueyi
 */
@Slf4j
public class AuthFilter implements WebFilter {

    private final RedisService redisService;

    private final IgnoreWhiteProperties ignoreWhite;

    public AuthFilter(RedisService redisService, IgnoreWhiteProperties ignoreWhite) {
        this.redisService = redisService;
        this.ignoreWhite = ignoreWhite;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String url = request.getURI().getPath();

        // TODO 暂时全放行
        if (true) {
            return chain.filter(exchange);
        }


        if (StrUtil.matches(url, ignoreWhite.getWhites())) {
            ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.ALLOW_LIST.getCode(), BaseConstants.Whether.YES.getCode());
            return chain.filter(exchange);
        }
        String token = ServletUtil.getToken(request);
        if (StrUtil.isEmpty(token)) {
            return ServletUtil.unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtil.parseToken(token);
        if (ObjectUtil.isNull(claims)) {
            return ServletUtil.unauthorizedResponse(exchange, "令牌已过期或验证不正确");
        }
        String userKey = JwtUtil.getUserKey(claims);
        String accountTypeKey = JwtUtil.getAccountType(claims);
        AccountType accountType = AccountType.getByCodeElseNull(accountTypeKey);
        if (ObjectUtil.isNull(accountType)) {
            return ServletUtil.unauthorizedResponse(exchange, "令牌已过期或验证不正确");
        }

        Boolean hasLogin = redisService.hasKey(ServletUtil.getTokenKey(userKey, accountType));
        if (!hasLogin) {
            return ServletUtil.unauthorizedResponse(exchange, "登录状态已过期");
        }

        switch (accountType) {
            case ADMIN, MEMBER -> {
                String enterpriseId = JwtUtil.getEnterpriseId(claims);
                String enterpriseName = JwtUtil.getEnterpriseName(claims);
                String isLessor = JwtUtil.getIsLessor(claims);
                String userId = JwtUtil.getUserId(claims);
                String userName = JwtUtil.getUserName(claims);
                String userType = JwtUtil.getUserType(claims);
                String sourceName = JwtUtil.getSourceName(claims);

                if (StrUtil.hasBlank(enterpriseId, enterpriseName, isLessor, userId, userName, userType, sourceName)) {
                    return ServletUtil.unauthorizedResponse(exchange, "令牌验证失败");
                }

                // 设置用户信息到请求
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode(), enterpriseId);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), enterpriseName);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.IS_LESSOR.getCode(), isLessor);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.USER_ID.getCode(), userId);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.USER_NAME.getCode(), userName);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.USER_TYPE.getCode(), userType);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), sourceName);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.USER_KEY.getCode(), userKey);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), accountTypeKey);
            }
            default -> {
                return ServletUtil.unauthorizedResponse(exchange, "令牌验证失败");
            }
        }

        // 内部请求来源参数清除
        ServletUtil.removeHeader(mutate, SecurityConstants.BaseSecurity.ALLOW_LIST.getCode());
        ServletUtil.removeHeader(mutate, SecurityConstants.BaseSecurity.FROM_SOURCE.getCode());
        return chain.filter(exchange);
    }

}