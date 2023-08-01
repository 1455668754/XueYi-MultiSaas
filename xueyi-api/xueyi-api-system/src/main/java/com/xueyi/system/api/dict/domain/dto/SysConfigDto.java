package com.xueyi.system.api.dict.domain.dto;

import com.xueyi.system.api.dict.domain.po.SysConfigPo;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 字典模块 | 参数 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigDto extends SysConfigPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 企业信息 */
    private SysEnterpriseDto enterpriseInfo;

}
