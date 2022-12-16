package com.xueyi.system.dict.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.model.SysDictTypeConverter;
import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.manager.ISysDictTypeManager;
import com.xueyi.system.dict.mapper.SysDictTypeMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.system.api.dict.domain.merge.MergeGroup.DICT_DATA_GROUP;

/**
 * 字典类型管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDictTypeManagerImpl extends BaseManagerImpl<SysDictTypeQuery, SysDictTypeDto, SysDictTypePo, SysDictTypeMapper, SysDictTypeConverter> implements ISysDictTypeManager {

    /**
     * 初始化从属关联关系
     *
     * @return 关系对象集合
     */
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>(){{
            add(new SlaveRelation(DICT_DATA_GROUP, SysDictDataManagerImpl.class));
        }};
    }

    /**
     * 校验字典编码是否唯一
     *
     * @param Id   字典类型Id
     * @param code 字典类型编码
     * @return 字典类型对象
     */
    @Override
    public SysDictTypeDto checkDictCodeUnique(Long Id, String code) {
        SysDictTypePo dictType = baseMapper.selectOne(
                Wrappers.<SysDictTypePo>query().lambda()
                        .ne(SysDictTypePo::getId, Id)
                        .eq(SysDictTypePo::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(dictType);
    }

}
