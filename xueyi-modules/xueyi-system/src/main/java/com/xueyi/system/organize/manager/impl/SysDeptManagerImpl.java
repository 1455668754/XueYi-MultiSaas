package com.xueyi.system.organize.manager.impl;

import com.xueyi.common.web.entity.manager.impl.TreeManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.organize.domain.model.SysDeptConverter;
import com.xueyi.system.organize.manager.ISysDeptManager;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 组织模块 | 部门管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDeptManagerImpl extends TreeManagerImpl<SysDeptQuery, SysDeptDto, SysDeptPo, SysDeptMapper, SysDeptConverter> implements ISysDeptManager {
}
