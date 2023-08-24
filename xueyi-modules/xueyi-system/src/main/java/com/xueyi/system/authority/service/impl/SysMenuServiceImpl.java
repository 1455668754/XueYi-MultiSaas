package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.IdUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.TreeServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.domain.correlate.SysMenuCorrelate;
import com.xueyi.system.authority.manager.ISysMenuManager;
import com.xueyi.system.authority.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 系统服务 | 权限模块 | 菜单管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysMenuServiceImpl extends TreeServiceImpl<SysMenuQuery, SysMenuDto, SysMenuCorrelate, ISysMenuManager> implements ISysMenuService {

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysMenuCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.SELECT, SysMenuCorrelate.INFO_LIST);
            put(CorrelateConstants.ServiceType.DELETE, SysMenuCorrelate.BASE_DEL);
        }};
    }

    /**
     * 查询菜单对象列表 | 数据权限 | 附加数据
     *
     * @param menu 菜单对象
     * @return 菜单对象集合
     */
    @Override
    @DataScope(userAlias = CREATE_BY, mapperScope = {"SysMenuMapper"})
    public List<SysMenuDto> selectListScope(SysMenuQuery menu) {
        return super.selectListScope(menu);
    }

    /**
     * 根据模块Id查询菜单路由
     *
     * @param moduleId 模块Id
     * @param menuIds  菜单Ids
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getRoutes(Long moduleId, Collection<Long> menuIds) {
        return baseManager.getRoutes(moduleId, menuIds);
    }

    /**
     * 获取企业有权限且状态正常的菜单
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 菜单对象集合
     */
    @Override
    public List<SysMenuDto> selectEnterpriseList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType) {
        return baseManager.selectEnterpriseList(authGroupIds, roleIds, isLessor, userType);
    }

    /**
     * 新增菜单对象
     *
     * @param menu 菜单对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysMenuDto menu) {
        menu.setName(IdUtil.simpleUUID());
        return super.insert(menu);
    }

    /**
     * 新增菜单对象（批量）
     *
     * @param menuList 菜单对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insertBatch(Collection<SysMenuDto> menuList) {
        if (CollUtil.isNotEmpty(menuList)) {
            menuList.forEach(menu -> menu.setName(IdUtil.simpleUUID()));
        }
        return super.insertBatch(menuList);
    }
}
