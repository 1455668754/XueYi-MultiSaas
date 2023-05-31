package com.xueyi.system.authority.service.impl;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.authority.correlate.SysModuleCorrelate;
import com.xueyi.system.authority.manager.ISysModuleManager;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 系统服务 | 权限模块 | 模块管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysModuleServiceImpl extends BaseServiceImpl<SysModuleQuery, SysModuleDto, SysModuleCorrelate, ISysModuleManager> implements ISysModuleService {

    /**
     * 当前用户首页可展示的模块路由
     *
     * @param roleIds 角色Ids
     * @return 模块集合
     */
    @Override
    public List<SysModuleDto> getRoutes(Set<Long> roleIds) {
        return baseManager.getRoutes(roleIds);
    }

    /**
     * 查询模块对象列表 | 数据权限 | 附加数据
     *
     * @param module 模块对象
     * @return 模块对象集合
     */
    @Override
    @DataScope(userAlias = CREATE_BY, mapperScope = {"SysModuleMapper"})
    public List<SysModuleDto> selectListScope(SysModuleQuery module) {
        return super.selectListScope(module);
    }

}
