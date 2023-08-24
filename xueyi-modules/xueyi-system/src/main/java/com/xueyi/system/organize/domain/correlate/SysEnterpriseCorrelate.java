package com.xueyi.system.organize.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.authority.domain.merge.SysTenantAuthGroupMerge;
import com.xueyi.system.authority.mapper.merge.SysTenantAuthGroupMergeMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.EDIT;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 组织模块 | 企业 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysEnterpriseCorrelate implements CorrelateService {

    AUTH_GROUP_SINGLE("查询|（企业-企业权限组关联）", new ArrayList<>() {{
        // 企业 | 企业-企业权限组关联
        add(new Indirect<>(SELECT, SysTenantAuthGroupMergeMapper.class, SysTenantAuthGroupMerge::getTenantId, SysTenantAuthGroupMerge::getAuthGroupId, SysEnterpriseDto::getId, SysEnterpriseDto::getAuthGroupIds));
    }}),
    AUTH_GROUP_EDIT("修改|（企业-企业权限组关联）", new ArrayList<>() {{
        // 企业 | 企业-企业权限组关联
        add(new Indirect<>(SELECT, SysTenantAuthGroupMergeMapper.class, SysTenantAuthGroupMerge::getTenantId, SysTenantAuthGroupMerge::getAuthGroupId, SysEnterpriseDto::getId, SysEnterpriseDto::getAuthGroupIds));
        // 企业 | 企业-企业权限组关联
        add(new Indirect<>(EDIT, SysTenantAuthGroupMergeMapper.class, SysTenantAuthGroupMerge::getTenantId, SysTenantAuthGroupMerge::getAuthGroupId, SysEnterpriseDto::getId, SysEnterpriseDto::getAuthGroupIds));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}