package com.xueyi.tenant.api.tenant.domain.dto;

import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.tenant.domain.po.TeStrategyPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据源策略 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeStrategyDto extends TeStrategyPo {

    private static final long serialVersionUID = 1L;

    /** 数据源信息 */
    private TeSourceDto source;

}