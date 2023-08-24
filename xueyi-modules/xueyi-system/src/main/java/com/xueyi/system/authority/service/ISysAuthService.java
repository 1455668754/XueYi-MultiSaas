package com.xueyi.system.authority.service;

import com.xueyi.system.authority.domain.vo.SysAuthTree;

import java.util.List;

/**
 * 权限管理 服务层
 *
 * @author xueyi
 */
public interface ISysAuthService {

    /**
     * 获取公共模块|菜单权限树
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectCommonAuthScope();

    /**
     * 获取企业模块|菜单权限树
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectEnterpriseAuthScope();

}
