package com.xueyi.system.monitor.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.domain.monitor.dto.SysOperationLogDto;
import com.xueyi.system.monitor.manager.SysOperationLogManager;
import com.xueyi.system.monitor.mapper.SysOperationLogMapper;
import com.xueyi.system.monitor.service.ISysOperationLogService;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 操作日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysOperationLogServiceImpl extends BaseServiceImpl<SysOperationLogDto, SysOperationLogManager, SysOperationLogMapper> implements ISysOperationLogService {

    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     * @return 结果
     */
    @Override
    @DS("#operationLog.sourceName")
    public int insert(SysOperationLogDto operationLog) {
        return baseManager.insert(operationLog);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperationLog() {
        baseManager.cleanOperationLog(SecurityUtils.getEnterpriseId());
    }
}
