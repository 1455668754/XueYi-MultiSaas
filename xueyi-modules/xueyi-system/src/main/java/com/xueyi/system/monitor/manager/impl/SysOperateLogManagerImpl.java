package com.xueyi.system.monitor.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.monitor.domain.model.SysOperateLogConverter;
import com.xueyi.system.api.log.domain.po.SysOperateLogPo;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import com.xueyi.system.monitor.manager.ISysOperateLogManager;
import com.xueyi.system.monitor.mapper.SysOperateLogMapper;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 监控模块 | 操作日志管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysOperateLogManagerImpl extends BaseManagerImpl<SysOperateLogQuery, SysOperateLogDto, SysOperateLogPo, SysOperateLogMapper, SysOperateLogConverter> implements ISysOperateLogManager {

    /**
     * 清空系统操作日志
     */
    @Override
    public void cleanOperateLog() {
        baseMapper.delete(Wrappers.query());
    }

    /**
     * 查询条件构造 | 列表查询
     *
     * @param operateLog 数据查询对象
     */
    @Override
    protected LambdaQueryWrapper<SysOperateLogPo> selectListQuery(SysOperateLogQuery operateLog) {
        return Wrappers.<SysOperateLogPo>query(operateLog).lambda()
                .func(i -> {
                    if (ObjectUtil.isAllNotEmpty(operateLog.getOperateTimeStart(), operateLog.getOperateTimeEnd())) {
                        i.between(SysOperateLogPo::getOperateTime, operateLog.getOperateTimeStart(), operateLog.getOperateTimeEnd());
                    }
                });
    }
}
