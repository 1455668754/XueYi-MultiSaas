package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.service.TokenUserService;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;
import com.xueyi.system.organize.service.ISysOrganizeService;
import com.xueyi.system.organize.service.ISysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class SysUserController extends BaseController<SysUserQuery, SysUserDto, ISysUserService> {

    @Autowired
    private ISysOrganizeService organizeService;

    @Autowired
    private TokenUserService tokenService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "用户";
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
        baseService.userDesensitized(loginUser.getUser());
        HashMap<String, Object> map = new HashMap<>();
        map.put("enterprise", loginUser.getEnterprise());
        map.put("user", loginUser.getUser());
        DataScope dataScope = tokenService.getDataScope();
        map.put("roles", dataScope.getRoles());
        map.put("permissions", dataScope.getPermissions());
        map.put("routes", tokenService.getRouteURL());
        return success(map);
    }

    /**
     * 查询用户列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_LIST)")
    public AjaxResult list(SysUserQuery user) {
        startPage();
        List<SysUserDto> list = baseService.selectListScope(user);
        list.forEach(item -> baseService.userDesensitized(item));
        return getDataTable(list);
    }

    /**
     * 查询用户详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询用户关联的角色Id集
     */
    @GetMapping(value = "/auth/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_AUTH)")
    public AjaxResult getRoleAuth(@PathVariable Long id) {
        return success(organizeService.selectUserRoleMerge(id));
    }

    /**
     * 用户导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_EXPORT)")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysUserQuery user) {
        super.export(response, user);
    }

    /**
     * 用户新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_ADD)")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysUserDto user) {
        return super.add(user);
    }

    /**
     * 用户修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_EDIT)")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysUserDto user) {
        return super.edit(user);
    }

    /**
     * 修改用户关联的角色Id集
     */
    @PutMapping(value = "/auth")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_AUTH)")
    public AjaxResult editRoleAuth(@RequestBody SysUserDto user) {
        organizeService.editUserRoleMerge(user.getId(), user.getRoleIds());
        return success();
    }

    /**
     * 用户修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_USER_EDIT, @Auth.SYS_USER_ES)")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysUserDto user) {
        return super.editStatus(user);
    }

    /**
     * 用户批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_DEL)")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_USER_RESET_PASSWORD)")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public AjaxResult resetPassword(@RequestBody SysUserDto user) {
        adminValidated(user.getId());
        return toAjax(baseService.resetUserPassword(user.getId(), SecurityUserUtils.encryptPassword(user.getPassword())));
    }

    /**
     * 获取用户选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysUserDto user) {

        if (operate.isEdit())
            adminValidated(user.getId());
        if (baseService.checkUserCodeUnique(user.getId(), user.getCode()))
            warn(StrUtil.format("{}{}{}失败，用户编码已存在", operate.getInfo(), getNodeName(), user.getNickName()));
        else if (baseService.checkUserNameUnique(user.getId(), user.getUserName()))
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
    private void adminValidated(Long Id) {
        if (!baseService.checkUserAllowed(Id))
            warn("不允许操作超级管理员用户");
    }
}
