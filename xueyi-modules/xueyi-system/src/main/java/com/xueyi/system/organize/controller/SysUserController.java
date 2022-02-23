package com.xueyi.system.organize.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 用户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController<SysUserDto, ISysUserService> {

    @Autowired
    private TokenService tokenService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "用户";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "岗位";
    }

    /**
     * 新增用户 | 内部调用
     */
    @InnerAuth
    @PostMapping("/inner/add")
    public R<SysUserDto> addInner(@RequestBody SysUserDto user) {
        return baseService.addInner(user) > 0 ? R.ok(user) : R.fail();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser();
        // 密码禁传
        loginUser.getUser().setPassword(null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", loginUser.getUser());
        map.put("roles", loginUser.getRoles());
        map.put("permissions", loginUser.getPermissions());
        map.put("routes", loginUser.getRouteMap());
        return AjaxResult.success(map);
    }

    /**
     * 查询用户列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions("organize:user:list")
    public AjaxResult listExtra(SysUserDto user) {
        return super.listExtra(user);
    }

    /**
     * 查询用户详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions("organize:user:single")
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 用户导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions("organize:user:export")
    public void export(HttpServletResponse response, SysUserDto user) {
        super.export(response, user);
    }

    /**
     * 用户新增
     */
    @Override
    @PostMapping
    @RequiresPermissions("organize:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysUserDto user) {
        return super.add(user);
    }

    /**
     * 用户修改
     */
    @Override
    @PutMapping
    @RequiresPermissions("organize:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysUserDto user) {
        return super.edit(user);
    }

    /**
     * 用户修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {"organize:user:edit", "organize:user:editStatus"}, logical = Logical.OR)
    @Log(title = "用户管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysUserDto user) {
        return super.editStatus(user);
    }

    /**
     * 用户批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions("organize:user:delete")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取用户选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

//    /**
//     * 修改用户-角色关系
//     */
//    @RequiresPermissions("system:role:set")
//    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/changeUserRole")
//    public AjaxResult editUserRole(@Validated @RequestBody SysUser user) {
//        if(baseService.checkUserAllowed(user.getUserId()))
//            return AjaxResult.error("禁止操作超级管理员");
//        return toAjax(baseService.updateUserRole(user));
//    }

    /**
     * 重置密码
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPassword")
    public AjaxResult resetPassword(@RequestBody SysUserDto user) {
        adminValidated(user.getId());
        return toAjax(baseService.resetUserPassword(user.getId(), SecurityUtils.encryptPassword(user.getPassword())));
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandleValidated(BaseConstants.Operate operate, SysUserDto user) {
        if (operate.isEdit())
            adminValidated(user.getId());
        if (baseService.checkUserCodeUnique(user.getId(), user.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，用户编码已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        else if (baseService.checkNameUnique(user.getId(), user.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，用户账号已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        else if (StringUtils.isNotEmpty(user.getEmail()) && baseService.checkPhoneUnique(user.getId(), user.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，手机号码已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        else if (StringUtils.isNotEmpty(user.getEmail()) && baseService.checkEmailUnique(user.getId(), user.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，邮箱账号已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        // 防止修改操作更替密码
        if (BaseConstants.Operate.ADD == operate)
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        else
            user.setPassword(null);
    }

    /**
     * 前置校验 （强制）修改状态
     *
     * @param user 用户对象
     */
    @Override
    protected void ESHandleValidated(BaseConstants.Operate operate, SysUserDto user) {
        adminValidated(user.getId());
    }

    /**
     * 前置校验 （强制）删除
     *
     * @param idList Id集合
     */
    @Override
    protected void RHandleValidated(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        Long userId = SecurityUtils.getUserId();
        // remove oneself or admin
        for (int i = idList.size() - 1; i >= 0; i--) {
            if (ObjectUtil.equals(idList.get(i), userId) || baseService.checkUserAllowed(idList.get(i)))
                idList.remove(i);
        }
        if (CollUtil.isEmpty(idList))
            throw new ServiceException("删除失败，不能删除自己或超管用户！");
        else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            throw new ServiceException("成功删除除自己及超管用户外的所有用户！");
        }
    }

    /**
     * 校验归属的岗位是否启用
     */
    private void adminValidated(Long Id) {
        if (!baseService.checkUserAllowed(Id))
            throw new ServiceException("不允许操作超级管理员用户");
    }
}
