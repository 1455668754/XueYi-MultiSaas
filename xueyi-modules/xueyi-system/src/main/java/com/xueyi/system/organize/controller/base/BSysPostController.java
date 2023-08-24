package com.xueyi.system.organize.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统服务 | 组织模块 | 岗位管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysPostController extends BaseController<SysPostQuery, SysPostDto, ISysPostService> {

    @Autowired
    protected ISysDeptService deptService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "岗位";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "部门";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysPostDto post) {
        switch (operate) {
            case EDIT_STATUS -> {
                if (StrUtil.equals(BaseConstants.Status.NORMAL.getCode(), post.getStatus())) {
                    SysPostDto original = baseService.selectById(post.getId());
                    if (BaseConstants.Status.DISABLE == deptService.checkStatus(original.getDeptId()))
                        warn(StrUtil.format("启用失败，该{}归属的{}已被禁用！", getNodeName(), getParentName()));
                }
            }
            case ADD, EDIT -> {
                if (baseService.checkNameUnique(post.getId(), post.getName()))
                    warn(StrUtil.format("{}{}{}失败，岗位名称已存在", operate.getInfo(), getNodeName(), post.getName()));
                if (BaseConstants.Status.DISABLE == deptService.checkStatus(post.getId()))
                    post.setStatus(BaseConstants.Status.DISABLE.getCode());
            }
        }
    }

}
