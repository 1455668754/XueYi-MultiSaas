package com.xueyi.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.tenant.mapper.TeSourceMapper;
import com.xueyi.tenant.service.ITeSeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class TeSeparationServiceImpl implements ITeSeparationService {

    @Autowired
    private TeSourceMapper teSourceMapper;

//    /**
//     * 查询 只读 数据源集合
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源集合
//     */
//    @Override
//    public List<TeSourceDto> mainSelectContainReadList(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainSelectContainReadList(teSourceDto);
//    }
//
//    /**
//     * 查询 含写 数据源集合
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源集合
//     */
//    @Override
//    public List<TeSourceDto> mainSelectContainWriteList(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainSelectContainWriteList(teSourceDto);
//    }
//
//    /**
//     * 根据Id查询数据源及其分离策略
//     *
//     * @param teSourceDto 数据源
//     * @return 数据源
//     */
//    @Override
//    public TeSourceDto mainSelectSeparationById(TeSourceDto teSourceDto) {
//        return teSourceMapper.mainSelectSeparationById(teSourceDto);
//    }
//
//    /**
//     * 修改数据源
//     *
//     * @param teSourceDto 数据源
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    public int mainUpdateSeparation(TeSourceDto teSourceDto) {
//        int k= teSourceMapper.mainDeleteSeparationBySourceId(teSourceDto);
//        if (teSourceDto.getValues() != null && teSourceDto.getValues().size() > 0) {
//            teSourceMapper.mainBatchSeparation(teSourceDto);
//        }
//        return k;
//    }
}