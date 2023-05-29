package com.xueyi.system.dict.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;

import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 字典模块 | 字典数据管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysDictDataManager extends IBaseManager<SysDictDataQuery, SysDictDataDto> {

    /**
     * 查询字典数据对象列表
     *
     * @param code 字典编码
     * @return 字典数据对象集合
     */
    List<SysDictDataDto> selectListByCode(String code);

    /**
     * 批量查询字典数据对象列表
     *
     * @param codes 字典编码集合
     * @return 字典数据对象集合
     */
    List<SysDictDataDto> selectListByCodes(Collection<String> codes);
}
