package com.xueyi.system.authority.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;

import java.util.List;
import java.util.Set;

/**
 * 系统服务 | 权限模块 | 模块管理 服务层
 *
 * @author xueyi
 */
public interface ISysModuleService extends IBaseService<SysModuleQuery, SysModuleDto> {

    /**
     * 获取企业有权限且状态正常的模块
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 模块对象集合
     */
    List<SysModuleDto> selectEnterpriseList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType);
}
