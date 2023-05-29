package com.xueyi.system.monitor.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.query.SysLoginLogQuery;

/**
 * 系统服务 | 监控模块 | 访问日志管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysLoginLogManager extends IBaseManager<SysLoginLogQuery, SysLoginLogDto> {

    /**
     * 清空系统登录日志
     */
    void cleanLoginLog();
}
