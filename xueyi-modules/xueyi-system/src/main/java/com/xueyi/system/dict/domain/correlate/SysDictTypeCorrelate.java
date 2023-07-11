package com.xueyi.system.dict.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.dict.service.ISysDictDataService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.*;

/**
 * 系统服务 | 字典模块 | 字典类型 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysDictTypeCorrelate implements CorrelateService {

    INFO_LIST("默认列表|（字典数据）", new ArrayList<>() {{
        // 字典类型 | 字典数据
        add(new Direct<>(SELECT, ISysDictDataService.class, SysDictDataDto::getValue, SysDictTypeDto::getId));
    }}),
    BASE_ADD("默认新增|（字典数据）", new ArrayList<>() {{
        // 字典类型 | 字典数据
        add(new Direct<>(ADD, ISysDictDataService.class, SysDictDataDto::getValue, SysDictTypeDto::getId));
    }}),
    BASE_EDIT("默认修改|（字典数据）", new ArrayList<>() {{
        // 字典类型 | 字典数据
        add(new Direct<>(EDIT, ISysDictDataService.class, SysDictDataDto::getValue, SysDictTypeDto::getId));
    }}),
    BASE_DEL("默认删除|（字典数据）", new ArrayList<>() {{
        // 字典类型 | 字典数据
        add(new Direct<>(DELETE, ISysDictDataService.class, SysDictDataDto::getValue, SysDictTypeDto::getId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
