package com.xueyi.file.api.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author xueyi
 */
@Data
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 文件Id */
    private Long id;

    /** 文件夹Id */
    private Long fid;

    /** 文件别称 */
    private String nick;

    /** 文件名称 */
    private String name;

    /** 文件地址 */
    private String url;

    /** 文件地址 */
    private float size;

}
