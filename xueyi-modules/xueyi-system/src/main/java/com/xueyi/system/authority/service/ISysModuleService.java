package com.xueyi.system.authority.service;

import com.xueyi.common.web.entity.service.ISubBaseService;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.model.LoginUser;

import java.util.List;

/**
 * 模块管理 服务层
 *
 * @author xueyi
 */
public interface ISysModuleService extends ISubBaseService<SysModuleDto, SysMenuDto> {

    /**
     * 当前用户首页可展示的模块路由
     *
     * @param loginUser 登录用户信息
     * @return 模块集合
     */
    List<SysModuleDto> getRoutes(LoginUser loginUser);
}
