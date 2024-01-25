package com.xueyi.system.dict.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;

/**
 * 系统服务 | 字典模块 | 参数管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysConfigManager extends IBaseManager<SysConfigQuery, SysConfigDto> {

    /**
     * 根据参数编码查询参数对象
     *
     * @param code 参数编码
     * @return 参数对象
     */
    SysConfigDto selectConfigByCode(String code);

    /**
     * 校验参数编码是否唯一
     *
     * @param id   参数Id
     * @param code 参数编码
     * @return 参数对象
     */
    SysConfigDto checkConfigCodeUnique(Long id, String code);

    /**
     * 校验是否为内置参数
     *
     * @param id 参数Id
     * @return 参数对象
     */
    SysConfigDto checkIsBuiltIn(Long id);

}
