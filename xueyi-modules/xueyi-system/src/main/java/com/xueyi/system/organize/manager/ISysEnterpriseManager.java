package com.xueyi.system.organize.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;

/**
 * 企业管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysEnterpriseManager extends IBaseManager<SysEnterpriseQuery, SysEnterpriseDto> {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    SysEnterpriseDto selectByName(String name);
}
