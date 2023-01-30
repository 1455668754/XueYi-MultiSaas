package com.xueyi.common.security.handler;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.service.RedisService;
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

    public SecurityContextHandler(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        String userKey = request.getHeaders().getFirst(SecurityConstants.BaseSecurity.USER_KEY.getCode());
        userKey = StrUtil.isNotEmpty(userKey) ? ServletUtil.urlDecode(userKey) : userKey;
        String accountType = request.getHeaders().getFirst(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
        if (StrUtil.isAllNotEmpty(userKey, accountType)) {
            Object loginUser = redisService.getCacheMapValue(ServletUtil.getTokenKey(userKey, accountType), SecurityConstants.BaseSecurity.LOGIN_USER.getCode());
            if (ObjectUtil.isNotNull(loginUser)) {
                return Mono.fromCallable(() -> new UsernamePasswordAuthenticationToken(loginUser, null)).map(SecurityContextImpl::new);
            }
        }
        return Mono.fromCallable(() -> new UsernamePasswordAuthenticationToken(new Object(), null)).map(SecurityContextImpl::new);
    }
}
