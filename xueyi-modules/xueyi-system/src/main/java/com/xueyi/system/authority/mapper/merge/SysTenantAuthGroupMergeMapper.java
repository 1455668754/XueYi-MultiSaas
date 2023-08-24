package com.xueyi.system.authority.mapper.merge;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BasicMapper;
import com.xueyi.system.authority.domain.merge.SysTenantAuthGroupMerge;

/**
 * 系统服务 | 权限模块 | 企业-企业权限组关联 数据层
 *
 * @author xueyi
 */
@Master
public interface SysTenantAuthGroupMergeMapper extends BasicMapper<SysTenantAuthGroupMerge> {
}