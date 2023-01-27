package com.xueyi.gateway.handler;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用户安全信息处理
 *
 * @author xueyi
 */
public class SecurityContextHandler implements ServerSecurityContextRepository {

    private final RedisService redisService;

    private final IgnoreWhiteProperties ignoreWhite;

    public SecurityContextHandler(RedisService redisService, IgnoreWhiteProperties ignoreWhite) {
        this.redisService = redisService;
        this.ignoreWhite = ignoreWhite;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        String userKey = ServletUtil.urlDecode(request.getHeaders().getFirst(SecurityConstants.BaseSecurity.USER_KEY.getCode()));
        String accountType = request.getHeaders().getFirst(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
        if (StrUtil.hasEmpty(userKey, accountType) && StrUtil.matches(url, ignoreWhite.getWhites())) {
            return Mono.fromCallable(() -> new UsernamePasswordAuthenticationToken(new Object(), null)).map(SecurityContextImpl::new);
        } else {
            Object loginUser = redisService.getCacheObject(ServletUtil.getTokenKey(userKey, accountType));
            if (ObjectUtil.isNotNull(loginUser)) {
                return Mono.fromCallable(() -> new UsernamePasswordAuthenticationToken(loginUser, null)).map(SecurityContextImpl::new);
            } else {
                return Mono.empty();
            }
        }
    }
}
