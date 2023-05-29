package com.xueyi.system.api.authority.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.feign.factory.RemoteMenuFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统服务 | 权限模块 | 菜单服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteMenuService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMenuFallbackFactory.class)
public interface RemoteMenuService {

    /**
     * 根据Id获取菜单信息
     *
     * @param id     菜单Id
     * @return 菜单对象
     */
    @GetMapping(value = "/menu/inner/{id}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<SysMenuDto> getInfoInner(@PathVariable("id") Long id);
}