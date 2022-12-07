package com.xueyi.system.api.dict.domain.query;

import com.xueyi.system.api.dict.domain.po.SysConfigPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 参数配置 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigQuery extends SysConfigPo {

    @Serial
    private static final long serialVersionUID = 1L;

}
