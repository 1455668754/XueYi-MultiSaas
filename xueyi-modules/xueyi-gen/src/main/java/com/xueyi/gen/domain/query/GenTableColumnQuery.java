package com.xueyi.gen.domain.query;

import com.xueyi.gen.domain.po.GenTableColumnPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 业务字段 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableColumnQuery extends GenTableColumnPo {

    @Serial
    private static final long serialVersionUID = 1L;

}