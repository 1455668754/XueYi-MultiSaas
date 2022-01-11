package com.xueyi.system.authority.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xueyi.common.web.entity.manager.SubBaseManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysModuleMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 模块管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysModuleManager extends SubBaseManager<SysModuleDto,SysModuleMapper, SysMenuDto, SysMenuMapper> {

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysMenuDto> queryWrapper, LambdaUpdateWrapper<SysMenuDto> updateWrapper, SysModuleDto module, Serializable key) {
        Serializable moduleId = ObjectUtil.isNotNull(module) ? module.getId() : key;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysMenuDto::getModuleId, moduleId);
        else
            updateWrapper.eq(SysMenuDto::getModuleId, moduleId);
    }
}
