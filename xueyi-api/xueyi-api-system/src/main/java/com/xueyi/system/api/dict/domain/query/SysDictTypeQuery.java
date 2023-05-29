package com.xueyi.system.api.dict.domain.query;

import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 字典模块 | 字典类型 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeQuery extends SysDictTypePo {

    @Serial
    private static final long serialVersionUID = 1L;

}
