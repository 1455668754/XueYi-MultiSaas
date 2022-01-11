package com.xueyi.system.authority.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.SubBaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.manager.SysModuleManager;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysModuleMapper;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 模块管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysModuleServiceImpl extends SubBaseServiceImpl<SysModuleDto, SysModuleManager, SysModuleMapper, SysMenuDto, ISysMenuService, SysMenuMapper> implements ISysModuleService {

    /**
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<SysMenuDto> menuList, SysMenuDto menu, SysModuleDto module, Serializable key) {
        Long moduleId = ObjectUtil.isNotNull(module) ? module.getId() : (Long) key;
        if (ObjectUtil.isNotNull(menu))
            menu.setModuleId(moduleId);
        else
            menuList.forEach(sub -> sub.setModuleId(moduleId));
    }

    /**
     * 当前用户首页可展示的模块路由
     *
     * @return 模块集合
     */
    @Override
    public List<SysModuleDto> getRoutes() {
//        Set<SysModuleDto> systemSet = AuthorityUtils.getSystemCache(SecurityUtils.getEnterpriseId());
        return null;
    }
}
