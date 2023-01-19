package com.xueyi.gateway.filter;

import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.basic.TenantConstants.AccountType;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 网关鉴权
 *
 * @author xueyi
 */
@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (StrUtil.isEmpty(token)) {
            unauthorizedResponse(request, response, "令牌不能为空");
            return;
        }
        Claims claims = JwtUtil.parseToken(token);
        if (ObjectUtil.isNull(claims)) {
            unauthorizedResponse(request, response, "令牌已过期或验证不正确");
            return;
        }
        String userKey = JwtUtil.getUserKey(claims);
        AccountType accountType = AccountType.getByCodeElseNull(JwtUtil.getAccountType(claims));
        if (ObjectUtil.isNull(accountType)) {
            unauthorizedResponse(request, response, "令牌已过期或验证不正确");
            return;
        }

        Object loginUser = redisService.getCacheObject(getTokenKey(userKey, accountType));
        if (ObjectUtil.isNotNull(loginUser)) {
            unauthorizedResponse(request, response, "登录状态已过期");
            return;
        }
        String enterpriseId = JwtUtil.getEnterpriseId(claims);
        String enterpriseName = JwtUtil.getEnterpriseName(claims);
        String isLessor = JwtUtil.getIsLessor(claims);
        String userId = JwtUtil.getUserId(claims);
        String userName = JwtUtil.getUserName(claims);
        String userType = JwtUtil.getUserType(claims);
        String sourceName = JwtUtil.getSourceName(claims);

        switch (accountType) {
            case ADMIN, MEMBER -> {
                if (StrUtil.hasBlank(enterpriseId, enterpriseName, isLessor, userId, userName, userType, sourceName)) {
                    unauthorizedResponse(request, response, "令牌验证失败");
                    return;
                }
            }
        }

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null));
        filterChain.doFilter(request, response);
    }

    /**
     * 鉴权失败处理
     */
    private void unauthorizedResponse(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        log.error("[鉴权异常处理]请求路径:{}", request.getServletPath());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpConstants.Status.UNAUTHORIZED.getCode());
        response.getWriter().println(AjaxResult.error(HttpConstants.Status.UNAUTHORIZED.getCode(), msg));
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token, AccountType accountType) {
        return switch (accountType) {
            case ADMIN -> CacheConstants.LoginTokenType.ADMIN.getCode() + token;
            case MEMBER -> CacheConstants.LoginTokenType.MEMBER.getCode() + token;
        };
    }

    /**
     * 获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotEmpty(token) && StrUtil.startWith(token, TokenConstants.PREFIX)) {
            token = StrUtil.replaceFirst(token, TokenConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }

}