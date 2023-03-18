package com.xueyi.system.dict.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;

/**
 * 参数配置管理 服务层
 *
 * @author xueyi
 */
public interface ISysConfigService extends IBaseService<SysConfigQuery, SysConfigDto> {

    /**
     * 根据参数编码查询参数值
     *
     * @param code 参数编码
     * @return 参数对象
     */
    SysConfigDto selectConfigByCode(String code);

    /**
     * 校验参数编码是否唯一
     *
     * @param Id         参数Id
     * @param configCode 参数编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkConfigCodeUnique(Long Id, String configCode);

    /**
     * 校验是否为内置参数
     *
     * @param Id 参数Id
     * @return 结果 | true/false 是/否
     */
    boolean checkIsBuiltIn(Long Id);
}