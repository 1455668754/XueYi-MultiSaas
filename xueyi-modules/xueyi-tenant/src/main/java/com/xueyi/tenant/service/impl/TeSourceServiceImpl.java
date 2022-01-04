package com.xueyi.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.domain.source.dto.TeSourceDto;
import com.xueyi.tenant.manager.TeSourceManager;
import com.xueyi.tenant.mapper.TeSourceMapper;
import com.xueyi.tenant.service.ITeSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据源管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TeSourceServiceImpl extends BaseServiceImpl<TeSourceDto, TeSourceManager, TeSourceMapper> implements ITeSourceService {

    @Autowired
    private TeSourceMapper teSourceMapper;

//    /**
//     * 查询数据源列表
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源
//     */
//    @Override
//    public List<TeSourceDto> mainSelectSourceList(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainSelectSourceList(teSourceDto);
//    }
//
//    /**
//     * 根据源Id查询数据源信息
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源
//     */
//    @Override
//    public TeSourceDto mainSelectSourceBySourceId(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainSelectSourceBySourceId(teSourceDto);
//    }
//
//    /**
//     * 新增数据源
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    @DataScope(ueAlias = "empty")
//    public int mainInsertSource(TeSourceDto teSourceDto) {
//        teSourceDto.setSlave((StringUtils.equals(TenantConstants.SourceType.READ.getCode(), teSourceDto.getType()) ? TenantConstants.Source.SLAVE.getCode() : TenantConstants.Source.MAIN.getCode()) + teSourceDto.getSnowflakeId().toString());
//        // 将数据新增的的数据源添加到数据源库
//        teSourceDto.setSyncType(TenantConstants.SyncType.ADD.getCode());
//        DSUtils.syncDs(teSourceDto);
//        if (teSourceDto.getType().equals(TenantConstants.SourceType.READ_WRITE.getCode())) {
//            TeSourceDto value = new TeSourceDto(teSourceDto.getSnowflakeId());
//            value.setSlave(teSourceDto.getSlave());
//            List<TeSourceDto> values = new ArrayList<>();
//            values.add(value);
//            teSourceDto.setValues(values);
//            teSourceDto.setSourceId(teSourceDto.getSnowflakeId());
//            teSourceMapper.mainBatchSeparation(teSourceDto);
//        }
//        return teSourceMapper.mainInsertSource(teSourceDto);
//    }
//
//    /**
//     * 修改数据源
//     *
//     * @param teSourceDto 数据源 | sourceId 数据源Id | name 数据源名称
//     * @return 结果
//     */
//    @Override
//    public int mainUpdateSource(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainUpdateSource(teSourceDto);
//    }
//
//    /**
//     * 启用/禁用数据源
//     *
//     * @param teSourceDto 数据源 | sourceId 数据源Id | status 状态 | isChange 系统默认
//     * @return 结果
//     */
//    @Override
//    public int mainUpdateSourceStatus(TeSourceDto teSourceDto) {
//        int rows = teSourceMapper.mainUpdateSourceStatus(teSourceDto);
//        if (rows > 0) {
//            DSUtils.syncDs(teSourceDto);
//        }
//        return rows;
//    }
//
//    /**
//     * 修改数据源排序
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    @Override
//    public int mainUpdateSourceSort(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainUpdateSourceSort(teSourceDto);
//    }
//
//    /**
//     * 删除数据源信息
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    public int mainDeleteSourceById(TeSourceDto teSourceDto) {
//        teSourceDto.setSyncType(TenantConstants.SyncType.DELETE.getCode());
//        DSUtils.syncDs(teSourceDto);
//        teSourceMapper.mainDeleteSeparationByValueId(teSourceDto);
//        return teSourceMapper.mainDeleteSourceById(teSourceDto);
//    }
//
//    /**
//     * 校验数据源是否已应用于策略
//     *
//     * @param teSourceDto 数据源
//     * @return 结果 (true 已应用 false 未应用)
//     */
//    @Override
//    public boolean mainCheckStrategySourceBySourceId(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainCheckStrategySourceBySourceId(teSourceDto) > 0;
//    }
//
//    /**
//     * 校验写数据源是否已设置主从配置
//     *
//     * @param teSourceDto 数据源
//     * @return 结果 (true 已设置 false 未设置)
//     */
//    @Override
//    public boolean mainCheckSeparationSourceByWriteId(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainCheckSeparationSourceByWriteId(teSourceDto) > 0;
//    }
//
//    /**
//     * 校验读数据源是否已应用于主从配置
//     *
//     * @param teSourceDto 数据源
//     * @return 结果 (true 已应用 false 未应用)
//     */
//    @Override
//    public boolean mainCheckSeparationSourceByReadId(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainCheckSeparationSourceByReadId(teSourceDto) > 0;
//    }
}