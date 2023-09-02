package com.xueyi.system.file.domain.dto;

import com.xueyi.file.api.domain.SysFile;
import com.xueyi.system.file.domain.po.SysFilePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 素材模块 | 文件 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileDto extends SysFilePo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 复制文件信息
     *
     * @param file 文件信息
     * @return 文件信息对象
     */
    public static SysFileDto copyFile(SysFile file) {
        SysFileDto fileInfo = new SysFileDto();
        fileInfo.setFolderId(file.getFolderId());
        fileInfo.setName(file.getName());
        fileInfo.setNick(file.getNick());
        fileInfo.setUrl(file.getUrl());
        fileInfo.setPath(file.getPath());
        fileInfo.setSize(file.getSize());
        fileInfo.setType(file.getType());
        return fileInfo;
    }
}