package com.xueyi.system.authority.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.web.entity.manager.SubBaseManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysModuleMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 模块管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysModuleManager extends SubBaseManager<SysModuleDto, SysModuleMapper, SysMenuDto, SysMenuMapper> {

    /**
     * 获取全部或指定范围内的状态正常公共模块
     *
     * @return 模块对象集合
     */
    public List<SysModuleDto> selectCommonList(Collection<? extends Serializable> idList) {
        return baseMapper.selectList(Wrappers.<SysModuleDto>query().lambda()
                .eq(SysModuleDto::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                .eq(SysModuleDto::getStatus, BaseConstants.Status.NORMAL.getCode())
                .func(i -> {
                    if (CollUtil.isNotEmpty(idList))
                        i.in(SysModuleDto::getId, idList);
                }));
    }

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
