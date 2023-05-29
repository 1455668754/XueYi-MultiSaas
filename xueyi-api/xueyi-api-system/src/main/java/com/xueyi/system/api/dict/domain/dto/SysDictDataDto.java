package com.xueyi.system.api.dict.domain.dto;

import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 字典模块 | 字典数据 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataDto extends SysDictDataPo {

    @Serial
    private static final long serialVersionUID = 1L;

}