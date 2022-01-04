package com.xueyi.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.domain.source.dto.TeStrategyDto;
import com.xueyi.tenant.manager.TeStrategyManager;
import com.xueyi.tenant.mapper.TeStrategyMapper;
import com.xueyi.tenant.service.ITeStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 源策略管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TeStrategyServiceImpl extends BaseServiceImpl<TeStrategyDto, TeStrategyManager, TeStrategyMapper> implements ITeStrategyService {

    @Autowired
    private TeStrategyMapper teStrategyMapper;

//    /**
//     * 查询数据源策略列表
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 数据源策略
//     */
//    @Override
//    public List<TeStrategyDto> mainSelectStrategyList(TeStrategyDto teStrategyDto) {
//        return teStrategyMapper.mainSelectStrategyList(teStrategyDto);
//    }
//
//    /**
//     * 查询数据源策略列表（排除停用）
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 数据源策略集合
//     */
//    @Override
//    public List<TeStrategyDto> mainSelectStrategyListExclude(TeStrategyDto teStrategyDto) {
//        return teStrategyMapper.mainSelectStrategyListExclude(teStrategyDto);
//    }
//
//    /**
//     * 查询数据源策略
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 数据源策略
//     */
//    @Override
//    public TeStrategyDto mainSelectStrategyById(TeStrategyDto teStrategyDto) {
//        return teStrategyMapper.mainSelectStrategyById(teStrategyDto);
//    }
//
//    /**
//     * 新增数据源策略
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    @DataScope(ueAlias = "empty")
//    public int mainInsertStrategy(TeStrategyDto teStrategyDto) {
//        int rows = teStrategyMapper.mainInsertStrategy(teStrategyDto);
//        if (teStrategyDto.getValues() != null && teStrategyDto.getValues().size() > 0) {
//            /* 获取生成雪花Id，并赋值给主键，加入至子表对应外键中 */
//            teStrategyDto.setStrategyId(teStrategyDto.getSnowflakeId());
//            teStrategyMapper.mainBatchSource(teStrategyDto);
//        }
//        return rows;
//    }
//
//    /**
//     * 修改数据源策略
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    public int mainUpdateStrategy(TeStrategyDto teStrategyDto) {
//        if (!StringUtils.equals(OrganizeConstants.STATUS_UPDATE_OPERATION, teStrategyDto.getUpdateType())) {
//            teStrategyMapper.mainDeleteSourceByStrategyId(teStrategyDto);
//            if (teStrategyDto.getValues() != null && teStrategyDto.getValues().size() > 0) {
//                teStrategyMapper.mainBatchSource(teStrategyDto);
//            }
//        }
//        return teStrategyMapper.mainUpdateStrategy(teStrategyDto);
//    }
//
//    /**
//     * 修改数据源策略排序
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 结果
//     */
//    @Override
//    public int mainUpdateStrategySort(TeStrategyDto teStrategyDto) {
//        return teStrategyMapper.mainUpdateStrategySort(teStrategyDto);
//    }
//
//    /**
//     * 批量删除数据源策略
//     *
//     * @param teStrategyDto 数据源策略
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    public int mainDeleteStrategyByIds(TeStrategyDto teStrategyDto) {
//        teStrategyMapper.mainDeleteSourceByStrategyIds(teStrategyDto);
//        return teStrategyMapper.mainDeleteStrategyByIds(teStrategyDto);
//    }
}