package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;
import com.xueyi.system.authority.domain.merge.SysTenantAuthGroupMerge;
import com.xueyi.system.authority.mapper.merge.SysTenantAuthGroupMergeMapper;
import com.xueyi.system.organize.domain.model.SysEnterpriseConverter;
import com.xueyi.system.organize.manager.ISysEnterpriseManager;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统服务 | 组织模块 | 企业管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysEnterpriseManagerImpl extends BaseManagerImpl<SysEnterpriseQuery, SysEnterpriseDto, SysEnterprisePo, SysEnterpriseMapper, SysEnterpriseConverter> implements ISysEnterpriseManager {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto selectByName(String name) {
        SysEnterprisePo enterprise = baseMapper.selectOne(
                Wrappers.<SysEnterprisePo>query().lambda()
                        .eq(SysEnterprisePo::getName, name)
                        .eq(SysEnterprisePo::getStatus, BaseConstants.Status.NORMAL.getCode()));
        return mapperDto(enterprise);
    }
}
