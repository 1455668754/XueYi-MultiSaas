package com.xueyi.system.organize.service;

import com.xueyi.system.organize.domain.vo.SysOrganizeTree;

import java.util.List;

/**
 * 组织管理 服务层
 *
 * @author xueyi
 */
public interface ISysOrganizeService {

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    List<SysOrganizeTree> selectOrganizeScope();
}
