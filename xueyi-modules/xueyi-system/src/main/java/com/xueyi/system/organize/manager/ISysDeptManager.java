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

    /**
     * 校验部门编码是否唯一
     *
     * @param Id   部门Id
     * @param code 部门编码
     * @return 部门对象
     */
    SysDeptDto checkDeptCodeUnique(Long Id, String code);
}
