package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.ISubTreeService;
import com.xueyi.system.api.domain.organize.dto.SysDeptDto;
import com.xueyi.system.api.domain.organize.dto.SysPostDto;

/**
 * 部门管理 服务层
 *
 * @author xueyi
 */
public interface ISysDeptService extends ISubTreeService<SysDeptDto, SysPostDto> {

    /**
     * 校验部门编码是否唯一
     *
     * @param Id   部门Id
     * @param code 部门编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkDeptCodeUnique(Long Id, String code);
}
