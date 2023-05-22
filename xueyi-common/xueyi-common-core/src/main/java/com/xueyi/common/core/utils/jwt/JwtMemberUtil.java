package com.xueyi.common.core.utils.jwt;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import io.jsonwebtoken.Claims;

/**
 * Jwt工具类 | 会员端
 *
 * @author xueyi
 */
public class JwtMemberUtil extends JwtUtil {

    /**
     * 根据令牌获取平台应用Id
     *
     * @param token 令牌
     * @return 平台应用Id
     */
    public static String getApplicationId(String token) {
        Claims claims = parseToken(token);
        return getApplicationId(claims);
    }

    /**
     * 根据令牌获取平台应用Id
     *
     * @param claims 身份信息
     * @return 平台应用Id
     */
    public static String getApplicationId(Claims claims) {
        return getValue(claims, SecurityConstants.MemberSecurity.APPLICATION_ID.getCode());
    }

    /**
     * 根据令牌获取应用AppId
     *
     * @param token 令牌
     * @return 应用AppId
     */
    public static String getAppId(String token) {
        Claims claims = parseToken(token);
        return getAppId(claims);
    }

    /**
     * 根据令牌获取应用AppId
     *
     * @param claims 身份信息
     * @return 应用AppId
     */
    public static String getAppId(Claims claims) {
        return getValue(claims, SecurityConstants.MemberSecurity.APP_ID.getCode());
    }
}
