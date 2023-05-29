package com.xueyi.system.authority.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.domain.query.SysClientQuery;

/**
 * 系统服务 | 权限模块 | 客户端管理 服务层
 *
 * @author xueyi
 */
public interface ISysClientService extends IBaseService<SysClientQuery, SysClientDto> {

    /**
     * 根据客户端Id查询客户端信息
     *
     * @param clientId 客户端Id
     * @return 客户端对象
     */
    SysClientDto selectByClientId(String clientId);
}
