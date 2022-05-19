package com.xueyi.system.organize.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManager;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.model.SysEnterpriseConverter;
import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;
import com.xueyi.system.organize.manager.ISysEnterpriseManager;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import org.springframework.stereotype.Component;

/**
 * 企业管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysEnterpriseManager extends BaseManager<SysEnterpriseQuery, SysEnterpriseDto, SysEnterprisePo, SysEnterpriseMapper, SysEnterpriseConverter> implements ISysEnterpriseManager {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto selectByName(String name) {
        return BeanUtil.copyProperties(baseMapper.selectOne(
                Wrappers.<SysEnterprisePo>query().lambda()
                        .eq(SysEnterprisePo::getName, name)
                        .eq(SysEnterprisePo::getStatus, BaseConstants.Status.NORMAL.getCode())), SysEnterpriseDto.class);
    }
}
