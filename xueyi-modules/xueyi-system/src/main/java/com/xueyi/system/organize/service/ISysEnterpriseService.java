package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;

/**
 * 系统服务 | 组织模块 | 企业管理 服务层
 *
 * @author xueyi
 */
public interface ISysEnterpriseService extends IBaseService<SysEnterpriseQuery, SysEnterpriseDto> {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    SysEnterpriseDto selectByName(String name);

    /**
     * 查询企业的权限组Id集
     *
     * @param id 企业Id
     * @return 企业信息对象
     */
    SysEnterpriseDto selectEnterpriseGroup(Long id);

    /**
     * 修改企业的权限组Id集
     *
     * @param enterprise 企业信息对象
     * @return 结果
     */
    int updateEnterpriseGroup(SysEnterpriseDto enterprise);
}
