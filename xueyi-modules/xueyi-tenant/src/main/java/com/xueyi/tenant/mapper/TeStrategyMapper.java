package com.xueyi.tenant.mapper;

import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.api.domain.source.dto.TeStrategyDto;

import java.util.List;

/**
 * 源策略管理 数据层
 *
 * @author xueyi
 */
public interface TeStrategyMapper extends BaseMapper<TeStrategyDto> {
    /**
     * 查询数据源策略列表
     *
     * @param teStrategyDto 数据源策略
     * @return 数据源策略集合
     */
    public List<TeStrategyDto> mainSelectStrategyList(TeStrategyDto teStrategyDto);

    /**
     * 查询数据源策略列表（排除停用）
     *
     * @param teStrategyDto 数据源策略
     * @return 数据源策略集合
     */
    public List<TeStrategyDto> mainSelectStrategyListExclude(TeStrategyDto teStrategyDto);

    /**
     * 查询数据源策略
     *
     * @param teStrategyDto 数据源策略 | strategyId 策略Id
     * @return 数据源策略
     */
    public TeStrategyDto mainSelectStrategyById(TeStrategyDto teStrategyDto);

    /**
     * 新增数据源策略
     * 访问控制 empty 租户更新（无前缀）()控制方法在impl层 | TenantStrategyServiceImpl)
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainInsertStrategy(TeStrategyDto teStrategyDto);

    /**
     * 修改数据源策略
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainUpdateStrategy(TeStrategyDto teStrategyDto);

    /**
     * 修改数据源策略排序
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainUpdateStrategySort(TeStrategyDto teStrategyDto);

    /**
     * 批量删除数据源策略
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainDeleteStrategyByIds(TeStrategyDto teStrategyDto);

    /**
     * 批量新增数据源
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainBatchSource(TeStrategyDto teStrategyDto);

    /**
     * 通过数据源策略Id删除数据源信息
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainDeleteSourceByStrategyId(TeStrategyDto teStrategyDto);

    /**
     * 批量删除数据源
     *
     * @param teStrategyDto 数据源策略
     * @return 结果
     */
    public int mainDeleteSourceByStrategyIds(TeStrategyDto teStrategyDto);
}