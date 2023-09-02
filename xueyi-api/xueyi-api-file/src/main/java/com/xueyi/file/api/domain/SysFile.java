package com.xueyi.file.api.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件信息
 *
 * @author xueyi
 */
@Data
public class SysFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 名称 */
    protected String name;

    /** 分类Id */
    protected Long folderId;

    /** 文件别名 */
    protected String nick;

    /** 文件地址 */
    protected String url;

    /** 存储路径 */
    protected String path;

    /** 文件大小 */
    protected Long size;

    /** 文件类型（0默认 1系统） */
    protected String type;

}
