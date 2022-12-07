package com.xueyi.system.api.dict.domain.query;

import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 字典数据 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataQuery extends SysDictDataPo {

    @Serial
    private static final long serialVersionUID = 1L;

}