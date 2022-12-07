package com.xueyi.system.api.model;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 源策略 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Source extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 源策略Id */
    Long sourceId;

    /** 主写源 */
    String master;

}