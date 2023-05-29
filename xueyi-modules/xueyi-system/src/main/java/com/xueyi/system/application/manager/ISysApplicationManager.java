package com.xueyi.system.application.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;

import java.util.List;

/**
 * 系统服务 | 应用模块 | 应用管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysApplicationManager extends IBaseManager<SysApplicationQuery, SysApplicationDto> {

    /**
     * 查询登录信息
     *
     * @param appId        应用Id
     * @param enterpriseId 企业Id
     * @return 数据对象集合
     */
    List<SysApplicationDto> selectListByLogin(String appId, Long enterpriseId);

    /**
     * 根据应用AppId查询数据对象
     *
     * @param appId 应用AppId
     * @return 数据对象
     */
    SysApplicationDto selectByAppId(String appId);
}