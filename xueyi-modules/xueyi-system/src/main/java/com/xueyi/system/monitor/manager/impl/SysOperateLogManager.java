package com.xueyi.system.monitor.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.impl.BaseManager;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.model.SysOperateLogConverter;
import com.xueyi.system.api.log.domain.po.SysOperateLogPo;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import com.xueyi.system.monitor.manager.ISysOperateLogManager;
import com.xueyi.system.monitor.mapper.SysOperateLogMapper;
import org.springframework.stereotype.Component;

/**
 * 操作日志管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysOperateLogManager extends BaseManager<SysOperateLogQuery, SysOperateLogDto, SysOperateLogPo, SysOperateLogMapper, SysOperateLogConverter> implements ISysOperateLogManager {

    /**
     * 清空系统操作日志
     */
    @Override
    public void cleanOperateLog() {
        baseMapper.delete(Wrappers.query());
    }
}
