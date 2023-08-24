package com.xueyi.system.organize.manager;

import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;

/**
 * 系统服务 | 组织模块 | 部门管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysDeptManager extends ITreeManager<SysDeptQuery, SysDeptDto> {
}
