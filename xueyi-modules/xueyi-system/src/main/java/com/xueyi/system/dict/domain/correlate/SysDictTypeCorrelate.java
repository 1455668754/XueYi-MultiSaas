package com.xueyi.system.dict.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;
import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.SELECT;

/**
 * 系统服务 | 字典模块 | 字典类型 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysDictTypeCorrelate implements CorrelateService {

    EN_INFO_SELECT("企业查询|（企业信息）", new ArrayList<>() {{
        // 字典类型 | 企业信息
        add(new Direct<>(SELECT, ISysEnterpriseService.class, SysDictTypeDto::getTenantId, SysEnterpriseDto::getId, SysDictTypeDto::getEnterpriseInfo));
    }}),
    CACHE_REFRESH("缓存|（字典数据）", new ArrayList<>() {{
        // 字典类型 | 字典数据
        add(new Direct<>(SELECT, ISysDictDataService.class, SysDictTypeDto::getCode, SysDictDataDto::getCode, SysDictTypeDto::getSubList));
    }}),
    BASE_DEL("默认删除|（字典数据）", new ArrayList<>() {{
        // 字典类型 | 字典数据
        add(new Direct<>(DELETE, ISysDictDataService.class, SysDictTypeDto::getCode, SysDictDataDto::getCode));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
