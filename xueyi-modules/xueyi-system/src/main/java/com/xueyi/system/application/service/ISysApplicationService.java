package com.xueyi.system.application.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;

/**
 * 系统服务 | 应用模块 | 应用管理 服务层
 *
 * @author xueyi
 */
public interface ISysApplicationService extends IBaseService<SysApplicationQuery, SysApplicationDto> {

    /**
     * 查询登录信息
     *
     * @param appId        应用Id
     * @param enterpriseId 企业Id
     * @return 结果
     */
     SysApplicationDto login(String appId, Long enterpriseId);

    /**
     * 根据应用AppId查询数据对象
     *
     * @param appId 应用AppId
     * @return 数据对象
     */
    SysApplicationDto selectByAppId(String appId);
}