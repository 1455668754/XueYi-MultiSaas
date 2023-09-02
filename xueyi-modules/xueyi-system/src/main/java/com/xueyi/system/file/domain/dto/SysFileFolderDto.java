package com.xueyi.system.file.domain.dto;

import com.xueyi.system.file.domain.po.SysFileFolderPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 素材模块 | 文件分类 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileFolderDto extends SysFileFolderPo {

    @Serial
    private static final long serialVersionUID = 1L;

}