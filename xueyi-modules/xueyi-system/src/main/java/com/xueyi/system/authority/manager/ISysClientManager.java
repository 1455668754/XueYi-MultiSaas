package com.xueyi.system.authority.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.domain.query.SysClientQuery;

/**
 * 系统服务 | 权限模块 | 客户端管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysClientManager extends IBaseManager<SysClientQuery, SysClientDto> {

    /**
     * 根据客户端Id查询客户端信息
     *
     * @param clientId 客户端Id
     * @return 客户端对象
     */
    SysClientDto selectByClientId(String clientId);
}
