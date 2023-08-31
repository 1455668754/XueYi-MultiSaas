package com.xueyi.system.api.authority.domain.dto;

import com.xueyi.system.api.authority.domain.po.SysModulePo;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 系统服务 | 权限模块 | 模块 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysModuleDto extends SysModulePo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 企业信息 */
    private SysEnterpriseDto enterpriseInfo;

    /** 菜单数据 */
    private List<SysMenuDto> subList;
}
