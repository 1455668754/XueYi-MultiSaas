package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.ITreeService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;

/**
 * 系统服务 | 组织模块 | 部门管理 服务层
 *
 * @author xueyi
 */
public interface ISysDeptService extends ITreeService<SysDeptQuery, SysDeptDto> {

    /**
     * 新增部门 | 内部调用
     *
     * @param dept 部门对象
     * @return 结果
     */
    int addInner(SysDeptDto dept);
}
