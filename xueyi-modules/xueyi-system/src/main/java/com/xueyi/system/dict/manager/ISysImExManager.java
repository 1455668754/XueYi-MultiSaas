package com.xueyi.system.dict.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.domain.query.SysImExQuery;

/**
 * 导入导出配置管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysImExManager extends IBaseManager<SysImExQuery, SysImExDto> {

    /**
     * 根据配置编码查询配置对象
     *
     * @param code 配置编码
     * @return 配置对象
     */
    SysImExDto selectByCode(String code);

    /**
     * 校验配置编码是否唯一
     *
     * @param id   配置Id
     * @param code 配置编码
     * @return 配置对象
     */
    SysImExDto checkCodeUnique(Long id, String code);

}