package com.xueyi.system.authority.manager;

import com.xueyi.common.web.entity.manager.ISubBaseManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;

import java.util.List;
import java.util.Set;

/**
 * 模块管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysModuleManager extends ISubBaseManager<SysModuleQuery, SysModuleDto, SysMenuQuery, SysMenuDto> {

    /**
     * 当前用户首页可展示的模块路由
     *
     * @param roleIds 角色Ids
     * @return 模块集合
     */
    List<SysModuleDto> getRoutes(Set<Long> roleIds);

    /**
     * 获取企业有权限的状态正常公共模块
     *
     * @return 模块对象集合
     */
    List<SysModuleDto> selectCommonList();

    /**
     * 获取租户有权限且状态正常的模块
     *
     * @return 模块对象集合
     */
    List<SysModuleDto> selectTenantList();

}
