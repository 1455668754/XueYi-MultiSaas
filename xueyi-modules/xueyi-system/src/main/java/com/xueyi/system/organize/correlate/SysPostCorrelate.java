package com.xueyi.system.organize.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.service.ISysDeptService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 组织模块 | 岗位 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysPostCorrelate implements CorrelateService {

    BASE_LIST_DEPT("列表|关联（部门）", new ArrayList<>() {{
        add(new Direct<>(SELECT, ISysDeptService.class, SysPostDto::getDeptId, SysDeptDto::getId, SysPostDto::getDept));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
