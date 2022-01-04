package com.xueyi.tenant.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.tenant.api.domain.source.dto.TeSourceDto;

/**
 * 数据源管理 服务层
 *
 * @author xueyi
 */
public interface ITeSourceService extends IBaseService<TeSourceDto> {

//    /**
//     * 查询数据源列表
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源集合
//     */
//    public List<TeSourceDto> mainSelectSourceList(TeSourceDto teSourceDto);
//
//    /**
//     * 根据源Id查询数据源信息
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源
//     */
//    public TeSourceDto mainSelectSourceBySourceId(TeSourceDto teSourceDto);
//
//    /**
//     * 新增数据源
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    public int mainInsertSource(TeSourceDto teSourceDto);
//
//    /**
//     * 修改数据源
//     *
//     * @param teSourceDto 数据源 | sourceId 数据源Id | name 数据源名称
//     * @return 结果
//     */
//    public int mainUpdateSource(TeSourceDto teSourceDto);
//
//    /**
//     * 启用/禁用数据源
//     *
//     * @param teSourceDto 数据源 | sourceId 数据源Id | status 状态 | isChange 系统默认
//     * @return 结果
//     */
//    public int mainUpdateSourceStatus(TeSourceDto teSourceDto);
//
//    /**
//     * 修改数据源排序
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    public int mainUpdateSourceSort(TeSourceDto teSourceDto);
//
//    /**
//     * 删除数据源信息
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    public int mainDeleteSourceById(TeSourceDto teSourceDto);
//
//    /**
//     * 校验数据源是否已应用于策略
//     *
//     * @param teSourceDto 数据源
//     * @return 结果 (true 已应用 false 未应用)
//     */
//    public boolean mainCheckStrategySourceBySourceId(TeSourceDto teSourceDto);
//
//    /**
//     * 校验写数据源是否已设置主从配置
//     *
//     * @param teSourceDto 数据源
//     * @return 结果 (true 已设置 false 未设置)
//     */
//    public boolean mainCheckSeparationSourceByWriteId(TeSourceDto teSourceDto);
//
//    /**
//     * 校验读数据源是否已应用于主从配置
//     *
//     * @param teSourceDto 数据源
//     * @return 结果 (true 已应用 false 未应用)
//     */
//    public boolean mainCheckSeparationSourceByReadId(TeSourceDto teSourceDto);
}