package com.xueyi.system.authority.manager.impl;

import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.model.SysAuthGroupConverter;
import com.xueyi.system.authority.domain.po.SysAuthGroupPo;
import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import com.xueyi.system.authority.manager.ISysAuthGroupManager;
import com.xueyi.system.authority.mapper.SysAuthGroupMapper;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 权限模块 | 企业权限组管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysAuthGroupManagerImpl extends BaseManagerImpl<SysAuthGroupQuery, SysAuthGroupDto, SysAuthGroupPo, SysAuthGroupMapper, SysAuthGroupConverter> implements ISysAuthGroupManager {
}