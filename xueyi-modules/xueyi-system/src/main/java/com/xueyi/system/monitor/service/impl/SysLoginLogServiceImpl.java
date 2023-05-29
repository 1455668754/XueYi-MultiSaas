package com.xueyi.system.monitor.service.impl;


import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.query.SysLoginLogQuery;
import com.xueyi.system.monitor.manager.ISysLoginLogManager;
import com.xueyi.system.monitor.service.ISysLoginLogService;
import org.springframework.stereotype.Service;

/**
 * 系统服务 | 监控模块 | 访问日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysLoginLogServiceImpl extends BaseServiceImpl<SysLoginLogQuery, SysLoginLogDto, ISysLoginLogManager> implements ISysLoginLogService {

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginLog() {
        baseManager.cleanLoginLog();
    }
}
