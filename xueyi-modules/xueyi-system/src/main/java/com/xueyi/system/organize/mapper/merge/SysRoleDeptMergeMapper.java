package com.xueyi.system.organize.mapper.merge;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BasicMapper;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;

/**
 * 系统服务 | 组织模块 | 角色-部门关联（权限范围） 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysRoleDeptMergeMapper extends BasicMapper<SysRoleDeptMerge> {
}