package com.xueyi.system.api.dict.domain.dto;

import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataDto extends SysDictDataPo {

    private static final long serialVersionUID = 1L;

}