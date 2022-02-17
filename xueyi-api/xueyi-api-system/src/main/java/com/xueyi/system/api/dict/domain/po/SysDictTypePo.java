package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;

/**
 * 字典类型 持久化对象
 *
 * @param <B> SubDto
 * @author xueyi
 */
public class SysDictTypePo<B> extends SubBaseEntity<B> {

    private static final long serialVersionUID = 1L;

    /** 字典类型 */
    @Excel(name = "字典类型")
    @TableField("code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
