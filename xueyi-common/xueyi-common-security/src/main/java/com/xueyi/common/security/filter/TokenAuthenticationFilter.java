package com.xueyi.common.security.filter;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.security.auth.AuthUtil;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * token认证过滤器
 *
 * @author xueyi
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authenticationToken = null;
        // 白名单
        if (BaseSecurityUtils.isAllowList(request)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
        } else {
            Object loginUser = AuthUtil.getLoginUserRefresh(request);
            if (ObjectUtil.isNotNull(loginUser))
                authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        }

        if (ObjectUtil.isNotNull(authenticationToken)) {
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }
}