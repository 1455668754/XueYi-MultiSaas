package com.xueyi.system.authority.service;

import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.common.web.entity.service.IBaseService;

import java.io.Serializable;

/**
 * 系统服务 | 权限模块 | 租户权限组管理 服务层
 *
 * @author xueyi
 */
public interface ISysAuthGroupService extends IBaseService<SysAuthGroupQuery, SysAuthGroupDto> {

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    SysAuthGroupDto selectInfoById(Serializable id);
}