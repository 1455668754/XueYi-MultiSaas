package com.xueyi.file.api.domain.dto;

import com.xueyi.file.api.domain.po.SysFilePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 文件 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileDto extends SysFilePo {

    @Serial
    private static final long serialVersionUID = 1L;

}