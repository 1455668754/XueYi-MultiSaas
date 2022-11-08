package com.xueyi.system.api.dict.domain.dto;

import com.xueyi.system.api.dict.domain.po.SysConfigPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 参数配置 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigDto extends SysConfigPo {

    private static final long serialVersionUID = 1L;

}
