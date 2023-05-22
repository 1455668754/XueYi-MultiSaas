package com.xueyi.common.core.utils.jwt;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import io.jsonwebtoken.Claims;

/**
 * Jwt工具类 | 平台端
 *
 * @author xueyi
 */
public class JwtPlatformUtil extends JwtUtil {

    /**
     * 根据令牌获取平台应用Id
     *
     * @param token 令牌
     * @return 平台应用Id
     */
    public static String getAppId(String token) {
        Claims claims = parseToken(token);
        return getAppId(claims);
    }

    /**
     * 根据令牌获取平台应用Id
     *
     * @param claims 身份信息
     * @return 平台应用Id
     */
    public static String getAppId(Claims claims) {
        return getValue(claims, SecurityConstants.PlatformSecurity.APP_ID.getCode());
    }
}
