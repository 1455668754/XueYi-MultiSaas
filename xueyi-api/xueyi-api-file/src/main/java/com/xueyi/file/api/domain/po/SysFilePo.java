package com.xueyi.file.api.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.xueyi.common.core.constant.basic.EntityConstants.REMARK;

/**
 * 文件 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_file", excludeProperty = { REMARK })
public class SysFilePo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类Id */
    @Excel(name = "分类Id")
    protected Long folderId;

    /** 文件别名 */
    @Excel(name = "文件别名")
    protected String nick;

    /** 文件地址 */
    @Excel(name = "文件地址")
    protected String url;

    /** 文件大小 */
    @Excel(name = "文件大小")
    protected Long size;

    /** 文件类型（0默认 1系统） */
    @Excel(name = "文件类型", readConverterExp = "0=默认,1=系统")
    protected String type;

}