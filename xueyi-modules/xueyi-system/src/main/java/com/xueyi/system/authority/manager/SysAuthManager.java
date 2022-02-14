package com.xueyi.system.authority.manager;

import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.domain.vo.SysAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysAuthManager {

    @Autowired
    private SysModuleManager moduleManager;

    @Autowired
    private SysMenuManager menuManager;

    /**
     * 获取公共模块|菜单权限树 | 租户端
     *
     * @return 权限对象集合
     */
    public List<SysAuthVo> selectCommonAuthScope() {
        List<SysModuleDto> modules = moduleManager.selectCommonList(null);
        List<SysMenuDto> menus = menuManager.selectCommonList(null);
        List<SysAuthVo> list = new ArrayList<>();
        list.addAll(modules.stream().map(SysAuthVo::new).collect(Collectors.toList()));
        list.addAll(menus.stream().map(SysAuthVo::new).collect(Collectors.toList()));
        return list;
    }
}
