package com.xueyi.gateway.filter;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 登录认证处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class SecurityFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String token = getToken(request);
        Claims claims = JwtUtil.parseToken(token);
        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode(), JwtUtil.getEnterpriseId(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), JwtUtil.getEnterpriseName(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.IS_LESSOR.getCode(), JwtUtil.getIsLessor(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_ID.getCode(), JwtUtil.getUserId(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_NAME.getCode(), JwtUtil.getUserName(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_TYPE.getCode(), JwtUtil.getUserType(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), JwtUtil.getSourceName(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_KEY.getCode(), JwtUtil.getUserKey(claims));
        addHeader(mutate, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), JwtUtil.getAccountType(claims));

        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null)
            return;
        String valueStr = value.toString();
        String valueEncode = ServletUtil.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotEmpty(token) && StrUtil.startWith(token, TokenConstants.PREFIX)) {
            token = StrUtil.replaceFirst(token, TokenConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
