package com.xueyi.tenant.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.api.domain.source.dto.TeSourceDto;

import java.util.List;

/**
 * 数据源管理 数据层
 *
 * @author xueyi
 */
public interface TeSourceMapper extends BaseMapper<TeSourceDto> {

    /**
     * 查询数据源列表
     *
     * @param teSourceDto 数据源
     * @return 数据源集合
     */
    public List<TeSourceDto> mainSelectSourceList(TeSourceDto teSourceDto);

    /**
     * 根据源Id查询数据源信息
     *
     * @param teSourceDto 数据源
     * @return 数据源
     */
    public TeSourceDto mainSelectSourceBySourceId(TeSourceDto teSourceDto);

    /**
     * 查询 只读 数据源集合
     *
     * @param teSourceDto 数据源
     * @return 数据源集合
     */
    public List<TeSourceDto> mainSelectContainReadList(TeSourceDto teSourceDto);

    /**
     * 查询 含写 数据源集合
     *
     * @param teSourceDto 数据源
     * @return 数据源集合
     */
    public List<TeSourceDto> mainSelectContainWriteList(TeSourceDto teSourceDto);

    /**
     * 根据Id查询数据源及其分离策略
     *
     * @param teSourceDto 数据源
     * @return 数据源
     */
    public TeSourceDto mainSelectSeparationById(TeSourceDto teSourceDto);

    /**
     * 新增数据源
     * 访问控制 empty 租户更新（无前缀） | 控制方法位于SourceServiceImpl mainInsertSource
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainInsertSource(TeSourceDto teSourceDto);

    /**
     * 修改数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param teSourceDto 数据源 | sourceId 数据源Id | name 数据源名称
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateSource(TeSourceDto teSourceDto);

    /**
     * 启用/禁用数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param teSourceDto 数据源 | sourceId 数据源Id | status 状态 | isChange 系统默认
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateSourceStatus(TeSourceDto teSourceDto);

    /**
     * 修改数据源排序
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainUpdateSourceSort(TeSourceDto teSourceDto);

    /**
     * 删除数据源
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainDeleteSourceById(TeSourceDto teSourceDto);

    /**
     * 批量删除数据源
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainDeleteSeparationByValueId(TeSourceDto teSourceDto);

    /**
     * 批量新增写-读数据源
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainBatchSeparation(TeSourceDto teSourceDto);

    /**
     * 通过写数据源Id删除读数据源关联信息
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainDeleteSeparationBySourceId(TeSourceDto teSourceDto);

    /**
     * 校验数据源是否已应用于策略
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainCheckStrategySourceBySourceId(TeSourceDto teSourceDto);

    /**
     * 校验写数据源是否已设置主从配置
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainCheckSeparationSourceByWriteId(TeSourceDto teSourceDto);

    /**
     * 校验读数据源是否已应用于主从配置
     *
     * @param teSourceDto 数据源
     * @return 结果
     */
    public int mainCheckSeparationSourceByReadId(TeSourceDto teSourceDto);
}