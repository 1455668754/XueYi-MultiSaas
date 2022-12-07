package com.xueyi.gen.domain.query;

import com.xueyi.gen.domain.po.GenTablePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 业务 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableQuery extends GenTablePo {

    @Serial
    private static final long serialVersionUID = 1L;

}