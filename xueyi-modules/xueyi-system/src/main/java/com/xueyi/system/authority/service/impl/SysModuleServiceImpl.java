package com.xueyi.system.authority.service.impl;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.authority.domain.correlate.SysModuleCorrelate;
import com.xueyi.system.authority.manager.ISysModuleManager;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysModuleCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.DELETE, SysModuleCorrelate.BASE_DEL);
        }};
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

    /**
     * 获取企业有权限且状态正常的模块
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 模块对象集合
     */
    @Override
    public List<SysModuleDto> selectEnterpriseList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType) {
        return baseManager.selectEnterpriseList(authGroupIds, roleIds, isLessor, userType);
    }
}
