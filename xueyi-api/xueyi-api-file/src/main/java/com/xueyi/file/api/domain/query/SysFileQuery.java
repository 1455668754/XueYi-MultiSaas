package com.xueyi.file.api.domain.query;

import com.xueyi.file.api.domain.po.SysFilePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 文件 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileQuery extends SysFilePo {

    @Serial
    private static final long serialVersionUID = 1L;
}