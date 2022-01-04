package com.xueyi.system.authority.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.TreeMapper;
import com.xueyi.system.api.domain.authority.dto.SysMenuDto;

/**
 * 菜单管理 数据层
 *
 * @author xueyi
 */
@DS("#main")
public interface SysMenuMapper extends TreeMapper<SysMenuDto> {
}
