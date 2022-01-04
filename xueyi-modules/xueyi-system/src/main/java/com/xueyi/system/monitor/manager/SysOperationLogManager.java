package com.xueyi.system.monitor.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.domain.monitor.dto.SysOperationLogDto;
import com.xueyi.system.monitor.mapper.SysOperationLogMapper;
import org.springframework.stereotype.Component;

/**
 * 操作日志管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysOperationLogManager extends BaseManager<SysOperationLogDto, SysOperationLogMapper> {

    /**
     * 清空系统操作日志
     */
    public void cleanOperationLog(Long enterpriseId) {
        baseMapper.delete(
                Wrappers.<SysOperationLogDto>update().lambda()
                        .eq(SysOperationLogDto::getEnterpriseId, enterpriseId));
    }

}
