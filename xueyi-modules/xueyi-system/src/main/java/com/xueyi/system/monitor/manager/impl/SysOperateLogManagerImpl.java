package com.xueyi.system.monitor.manager.impl;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
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
public class SysOperateLogManagerImpl extends BaseManagerImpl<SysOperateLogQuery, SysOperateLogDto, SysOperateLogPo, SysOperateLogMapper, SysOperateLogConverter> implements ISysOperateLogManager {

    /**
     * 清空系统操作日志
     */
    @Override
    public void cleanOperateLog() {
        baseMapper.delete(Wrappers.query());
    }

    /**
     * 查询条件附加
     *
     * @param selectType   查询类型
     * @param queryWrapper 条件构造器
     * @param operateLog   数据查询对象
     */
    @Override
    protected void SelectListQuery(BaseConstants.SelectType selectType, LambdaQueryWrapper<SysOperateLogPo> queryWrapper, SysOperateLogQuery operateLog) {
        if (ObjectUtil.isAllNotEmpty(operateLog.getOperateTimeStart(), operateLog.getOperateTimeEnd()))
            queryWrapper.between(SysOperateLogPo::getOperateTime, operateLog.getOperateTimeStart(), operateLog.getOperateTimeEnd());
    }
}
