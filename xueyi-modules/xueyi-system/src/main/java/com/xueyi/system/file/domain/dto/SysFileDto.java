package com.xueyi.system.file.domain.dto;

import com.xueyi.system.file.domain.po.SysFilePo;
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