package com.xueyi.system.application.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.application.model.SysApplicationConverter;
import com.xueyi.system.api.application.domain.po.SysApplicationPo;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;
import com.xueyi.system.application.manager.ISysApplicationManager;
import com.xueyi.system.application.mapper.SysApplicationMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.xueyi.common.core.constant.basic.SqlConstants.LIMIT_ONE;

/**
 * 系统服务 | 应用模块 | 应用管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysApplicationManager extends BaseManagerImpl<SysApplicationQuery, SysApplicationDto, SysApplicationPo, SysApplicationMapper, SysApplicationConverter> implements ISysApplicationManager {

    /**
     * 查询登录信息
     *
     * @param appId        应用Id
     * @param enterpriseId 企业Id
     * @return 数据对象集合
     */
    @Override
    @TenantIgnore
    public List<SysApplicationDto> selectListByLogin(String appId, Long enterpriseId) {
        List<SysApplicationPo> list = baseMapper.selectList(Wrappers.<SysApplicationPo>query().lambda()
                .eq(SysApplicationPo::getAppId, appId)
                .func(i -> {
                    if (ObjectUtil.isNotNull(enterpriseId))
                        i.eq(SysApplicationPo::getTenantId, enterpriseId);
                }));
        return mapperDto(list);
    }

    /**
     * 根据应用AppId查询数据对象
     *
     * @param appId 应用AppId
     * @return 数据对象
     */
    @Override
    public SysApplicationDto selectByAppId(String appId) {
        SysApplicationPo po = baseMapper.selectOne(Wrappers.<SysApplicationPo>query().lambda()
                .eq(SysApplicationPo::getAppId, appId)
                .last(LIMIT_ONE));
        return mapperDto(po);
    }
}