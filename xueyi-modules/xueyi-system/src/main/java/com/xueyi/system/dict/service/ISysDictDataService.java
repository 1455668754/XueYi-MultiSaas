package com.xueyi.system.dict.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 系统服务 | 字典模块 | 字典数据管理 服务层
 *
 * @author xueyi
 */
public interface ISysDictDataService extends IBaseService<SysDictDataQuery, SysDictDataDto> {

    /**
     * 查询全部字典数据列表 | 全局
     *
     * @param query 字典数据查询对象
     * @return 字典数据对象集合
     */
    List<SysDictDataDto> selectAllListScope(SysDictDataQuery query);

    /**
     * 根据Id查询单条数据对象 | 全局
     *
     * @param id Id
     * @return 数据对象
     */
    SysDictDataDto selectAllById(Serializable id);

    /**
     * 查询字典数据对象列表
     *
     * @param code 字典编码
     * @return 字典数据对象集合
     */
    List<SysDictDataDto> selectListByCode(String code);
}
