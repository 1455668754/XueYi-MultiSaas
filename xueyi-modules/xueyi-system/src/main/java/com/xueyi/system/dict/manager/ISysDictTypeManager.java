package com.xueyi.system.dict.manager;

import com.xueyi.common.web.entity.manager.ISubBaseManager;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;

/**
 * 字典类型管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysDictTypeManager extends ISubBaseManager<SysDictTypeQuery, SysDictTypeDto, SysDictDataQuery, SysDictDataDto> {

    /**
     * 校验字典编码是否唯一
     *
     * @param Id   字典类型Id
     * @param code 字典类型编码
     * @return 字典类型对象
     */
    SysDictTypeDto checkDictCodeUnique(Long Id, String code);
}
