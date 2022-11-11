package com.xueyi.gateway.filter;

import com.xueyi.common.core.constant.basic.*;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 *
 * @author xueyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (CollUtil.contains(ignoreWhite.getWhites(), url)) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StrUtil.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userKey = JwtUtil.getUserKey(claims);
        String accountType = JwtUtil.getAccountType(claims);
        boolean isLogin = redisService.hasKey(getTokenKey(userKey, accountType));
        if (!isLogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String enterpriseId = JwtUtil.getEnterpriseId(claims);
        String enterpriseName = JwtUtil.getEnterpriseName(claims);
        String isLessor = JwtUtil.getIsLessor(claims);
        String userId = JwtUtil.getUserId(claims);
        String userName = JwtUtil.getUserName(claims);
        String userType = JwtUtil.getUserType(claims);
        String sourceName = JwtUtil.getSourceName(claims);

        if (TenantConstants.AccountType.isAdmin(accountType) && StrUtil.hasBlank(enterpriseId, enterpriseName, isLessor, userId, userName, userType, sourceName)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }
        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode(), enterpriseId);
        addHeader(mutate, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), enterpriseName);
        addHeader(mutate, SecurityConstants.BaseSecurity.IS_LESSOR.getCode(), isLessor);
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_ID.getCode(), userId);
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_NAME.getCode(), userName);
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_TYPE.getCode(), userType);
        addHeader(mutate, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), sourceName);
        addHeader(mutate, SecurityConstants.BaseSecurity.USER_KEY.getCode(), userKey);
        addHeader(mutate, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), accountType);
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

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), msg, HttpConstants.Status.UNAUTHORIZED.getCode());
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token, String accountType) {
        return TenantConstants.AccountType.isAdmin(accountType) ? CacheConstants.LoginTokenType.ADMIN.getCode() + token : StrUtil.EMPTY;
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