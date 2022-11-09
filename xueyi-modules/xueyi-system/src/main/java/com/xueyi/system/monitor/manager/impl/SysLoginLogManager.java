package com.xueyi.system.monitor.manager.impl;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManager;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.model.SysLoginLogConverter;
import com.xueyi.system.api.log.domain.po.SysLoginLogPo;
import com.xueyi.system.api.log.domain.query.SysLoginLogQuery;
import com.xueyi.system.monitor.manager.ISysLoginLogManager;
import com.xueyi.system.monitor.mapper.SysLoginLogMapper;
import org.springframework.stereotype.Component;

/**
 * 访问日志管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysLoginLogManager extends BaseManager<SysLoginLogQuery, SysLoginLogDto, SysLoginLogPo, SysLoginLogMapper, SysLoginLogConverter> implements ISysLoginLogManager {

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginLog() {
        baseMapper.delete(Wrappers.update());
    }

    /**
     * 查询条件附加
     *
     * @param selectType   查询类型
     * @param queryWrapper 条件构造器
     * @param loginLog     数据查询对象
     */
    @Override
    protected void SelectListQuery(BaseConstants.SelectType selectType, LambdaQueryWrapper<SysLoginLogPo> queryWrapper, SysLoginLogQuery loginLog) {
        if (ObjectUtil.isAllNotEmpty(loginLog.getAccessTimeStart(), loginLog.getAccessTimeEnd()))
            queryWrapper.between(SysLoginLogPo::getAccessTime, loginLog.getAccessTimeStart(), loginLog.getAccessTimeEnd());
    }
}
