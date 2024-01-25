package com.xueyi.system.dict.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.domain.query.SysImExQuery;

/**
 * 导入导出配置管理 服务层
 *
 * @author xueyi
 */
public interface ISysImExService extends IBaseService<SysImExQuery, SysImExDto> {

    /**
     * 根据配置编码查询配置值
     *
     * @param code 配置编码
     * @return 配置对象
     */
    SysImExDto selectByCode(String code);

    /**
     * 更新缓存数据
     */
    Boolean syncCache();

    /**
     * 清空缓存数据
     */
    void clearCache();

    /**
     * 校验配置编码是否唯一
     *
     * @param id   配置Id
     * @param code 配置编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkCodeUnique(Long id, String code);
}