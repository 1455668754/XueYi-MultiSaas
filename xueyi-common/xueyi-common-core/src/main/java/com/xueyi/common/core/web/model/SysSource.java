package com.xueyi.common.core.web.model;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 源策略 基础数据对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSource extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 源策略Id */
    Long sourceId;

    /** 主写源 */
    String master;

}