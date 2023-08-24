package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;

import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 岗位管理 服务层
 *
 * @author xueyi
 */
public interface ISysPostService extends IBaseService<SysPostQuery, SysPostDto> {

    /**
     * 用户登录校验 | 根据部门Ids获取归属岗位对象集合
     *
     * @param deptIds 部门Ids
     * @return 岗位对象集合
     */
    List<SysPostDto> selectListByDeptIds(Collection<Long> deptIds);

    /**
     * 新增岗位 | 内部调用
     *
     * @param post 岗位对象
     * @return 结果
     */
    int addInner(SysPostDto post);
}
