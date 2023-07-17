package com.xueyi.system.monitor.controller.admin;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.monitor.domain.dto.SysUserOnline;
import com.xueyi.system.monitor.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统服务 | 监控模块 | 在线用户监控 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/online")
public class ASysUserOnlineController extends BasisController {

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ONLINE_LIST)")
    public AjaxResult list(String ipaddr, String userName) {
        String cacheKeyPrepend = StrUtil.format("{}:{}:{}:", CacheConstants.AUTHORIZATION, CacheConstants.LoginTokenType.ADMIN.getCode(), SecurityUserUtils.getEnterpriseId());
        String cacheKey = StrUtil.format("{}{}", cacheKeyPrepend, "*");
        Collection<String> keys = redisService.keys(cacheKey);
        List<SysUserOnline> userOnlineList = new ArrayList<>();
        Map<String, List<String>> userKeys = keys.stream().collect(Collectors.groupingBy(item -> {
            int index = StrUtil.ordinalIndexOf(item, ":", NumberUtil.Four);
            return NumberUtil.equals(index, NumberUtil.Ne_One) ? NumberUtil.Ne_One + StrUtil.EMPTY : StrUtil.subWithLength(item, NumberUtil.Zero, index);
        }));
        for (List<String> values : userKeys.values()) {
            if (CollUtil.isEmpty(values)) {
                continue;
            }
            String tokenKey = StrUtil.format(":{}:", OAuth2ParameterNames.REFRESH_TOKEN);
            String key = values.stream().filter(StrUtil::isNotBlank).filter(item -> StrUtil.contains(item, tokenKey)).findFirst().orElse(null);
            if (StrUtil.isBlank(key)) {
                continue;
            }
            LoginUser loginUser = redisService.getCacheMapValue(key, SecurityConstants.BaseSecurity.USER_INFO.getCode());
            if (StrUtil.isNotEmpty(ipaddr) && StrUtil.isNotEmpty(userName)) {
                if (StrUtil.equals(ipaddr, loginUser.getIpaddr()) && StrUtil.equals(userName, loginUser.getUserName())) {
                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, loginUser));
                }
            } else if (StrUtil.isNotEmpty(ipaddr)) {
                if (StrUtil.equals(ipaddr, loginUser.getIpaddr())) {
                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, loginUser));
                }
            } else if (StrUtil.isNotEmpty(userName)) {
                if (StrUtil.equals(userName, loginUser.getUserName())) {
                    userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, loginUser));
                }
            } else {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_ONLINE_FORCE_LOGOUT)")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    public AjaxResult forceLogout(@PathVariable List<String> idList) {
        if (ArrayUtil.isNotEmpty(idList)) {
            idList.forEach(id -> redisService.deleteObject(CacheConstants.LoginTokenType.ADMIN.getCode() + id));
        }
        return success();
    }
}
