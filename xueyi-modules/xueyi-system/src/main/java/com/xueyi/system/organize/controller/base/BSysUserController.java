package com.xueyi.system.organize.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;
import com.xueyi.system.organize.service.ISysUserService;

import java.util.List;

/**
 * 用户管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysUserController extends BaseController<SysUserQuery, SysUserDto, ISysUserService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "用户";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysUserDto user) {

        if (operate.isEdit())
            adminValidated(user.getId());
        if (baseService.checkUserNameUnique(user.getId(), user.getUserName()))
            warn(StrUtil.format("{}{}{}失败，用户账号已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        else if (StrUtil.isNotEmpty(user.getEmail()) && baseService.checkPhoneUnique(user.getId(), user.getCode()))
            warn(StrUtil.format("{}{}{}失败，手机号码已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        else if (StrUtil.isNotEmpty(user.getEmail()) && baseService.checkEmailUnique(user.getId(), user.getName()))
            warn(StrUtil.format("{}{}{}失败，邮箱账号已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        switch (operate) {
            case ADD:
                // 防止修改操作更替密码
                user.setPassword(SecurityUserUtils.encryptPassword(user.getPassword()));
                break;
            case EDIT_STATUS:
                adminValidated(user.getId());
                break;
        }
    }

    /**
     * 前置校验 （强制）删除
     *
     * @param idList Id集合
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        Long userId = SecurityUserUtils.getUserId();
        // remove oneself or admin
        for (int i = idList.size() - 1; i >= 0; i--)
            if (ObjectUtil.equals(idList.get(i), userId) || !baseService.checkUserAllowed(idList.get(i)))
                idList.remove(i);
        if (CollUtil.isEmpty(idList))
            warn("删除失败，不能删除自己或超管用户！");
        else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            warn("成功删除除自己及超管用户外的所有用户！");
        }
    }

    /**
     * 校验归属的岗位是否启用
     */
    protected void adminValidated(Long Id) {
        if (!baseService.checkUserAllowed(Id))
            warn("不允许操作超级管理员用户");
    }
}
