package com.xueyi.system.api.organize.domain.dto;

import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 组织模块 | 企业 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysEnterpriseDto extends SysEnterprisePo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 企业权限组Ids */
    private Long[] authGroupIds;
}
