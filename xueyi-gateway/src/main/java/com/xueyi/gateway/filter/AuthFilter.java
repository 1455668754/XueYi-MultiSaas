package com.xueyi.gateway.filter;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants.AccountType;
import com.xueyi.common.core.constant.basic.TokenConstants;
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

        if (StrUtil.matches(url, ignoreWhite.getWhites())) {
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
        String accessTokenPrefix = JwtUtil.getAccessKey(claims);
        if (StrUtil.isBlank(accessTokenPrefix) || !StrUtil.startWith(accessTokenPrefix, TokenConstants.PREFIX)) {
            return ServletUtil.unauthorizedResponse(exchange, "令牌已过期或验证不正确");
        }
        String accessToken = StrUtil.replaceFirst(accessTokenPrefix, TokenConstants.PREFIX, StrUtil.EMPTY);
        String userKey = JwtUtil.getUserKey(claims);
        if (StrUtil.isBlank(userKey)) {
            return ServletUtil.unauthorizedResponse(exchange, "令牌已过期或验证不正确");
        }
        AccountType accountType = AccountType.getByCodeElseNull(JwtUtil.getAccountType(claims));
        if (ObjectUtil.isNull(accountType)) {
            return ServletUtil.unauthorizedResponse(exchange, "令牌已过期或验证不正确");
        }

        Boolean hasLogin = redisService.hasKey(accessToken) && redisService.hasKey(userKey);
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
                String nickName = JwtUtil.getNickName(claims);
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
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.NICK_NAME.getCode(), nickName);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.USER_TYPE.getCode(), userType);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), sourceName);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode(), accessTokenPrefix);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.REFRESH_TOKEN.getCode(), userKey);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.USER_KEY.getCode(), userKey);
                ServletUtil.addHeader(mutate, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), accountType.getCode());
            }
            default -> {
                return ServletUtil.unauthorizedResponse(exchange, "令牌验证失败");
            }
        }

        // 内部请求来源参数清除
        ServletUtil.removeHeader(mutate, SecurityConstants.BaseSecurity.FROM_SOURCE.getCode());
        return chain.filter(exchange);
    }

}