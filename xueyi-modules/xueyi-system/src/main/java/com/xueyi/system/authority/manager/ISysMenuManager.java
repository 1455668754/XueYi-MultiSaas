package com.xueyi.system.authority.manager;

import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统服务 | 权限模块 | 菜单管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysMenuManager extends ITreeManager<SysMenuQuery, SysMenuDto> {

    /**
     * 获取全部状态正常公共菜单
     *
     * @return 菜单对象集合
     */
    List<SysMenuDto> selectCommonList();

    /**
     * 获取企业有权限且状态正常的菜单
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 菜单对象集合
     */
    List<SysMenuDto> selectEnterpriseList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType);

    /**
     * 根据模块Id查询菜单路由
     *
     * @param moduleId 模块Id
     * @param menuIds  菜单Ids
     * @return 菜单列表
     */
    List<SysMenuDto> getRoutes(Long moduleId, Collection<Long> menuIds);
}
