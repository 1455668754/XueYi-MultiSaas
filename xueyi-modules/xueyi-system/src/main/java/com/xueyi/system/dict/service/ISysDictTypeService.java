package com.xueyi.system.dict.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;

import java.util.List;

/**
 * 系统服务 | 字典模块 | 字典类型管理 服务层
 *
 * @author xueyi
 */
public interface ISysDictTypeService extends IBaseService<SysDictTypeQuery, SysDictTypeDto> {

    /**
     * 查询全部字典数据列表
     *
     * @param query 字典数据查询对象
     * @return 字典数据对象集合
     */
    List<SysDictTypeDto> selectAllListScope(SysDictTypeQuery query);

    /**
     * 更新缓存数据
     */
    Boolean syncCache();

    /**
     * 校验字典编码是否唯一
     *
     * @param Id       字典类型Id
     * @param dictCode 字典类型编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkDictCodeUnique(Long Id, String dictCode);
}
