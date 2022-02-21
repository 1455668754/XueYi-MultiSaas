package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.manager.SysOrganizeManager;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 组织管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysOrganizeServiceImpl implements ISysOrganizeService {

    @Autowired
    private SysOrganizeManager organizeManager;

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeScope() {
        return organizeManager.selectOrganizeScope();
    }
}
