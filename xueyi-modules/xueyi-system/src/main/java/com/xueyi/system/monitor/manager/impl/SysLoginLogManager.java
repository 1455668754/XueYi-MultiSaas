package com.xueyi.system.monitor.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
}
