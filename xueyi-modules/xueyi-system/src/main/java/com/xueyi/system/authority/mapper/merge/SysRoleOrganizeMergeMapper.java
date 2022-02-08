package com.xueyi.system.authority.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.authority.domain.merge.SysRoleOrganizeMerge;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 角色-组织关联（权限范围） 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysRoleOrganizeMergeMapper extends BaseMapper<SysRoleOrganizeMerge> {
}
