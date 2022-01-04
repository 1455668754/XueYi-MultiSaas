package com.xueyi.system.organize.controller;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.api.domain.material.SysFile;
import com.xueyi.system.api.domain.organize.dto.SysUserDto;
import com.xueyi.system.api.feign.RemoteFileService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 个人信息管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/user/profile")
public class SysProfileController extends BasisController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private ISysUserService userService;
    
    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.getUser().setPassword(null);
        return AjaxResult.success(loginUser);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息管理 - 基本信息修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUserDto user) {
        if (StringUtils.isNotEmpty(user.getPhone())
                && userService.checkPhoneUnique(user.getId(), user.getPhone())) {
            return AjaxResult.error("该手机号码已被使用");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && userService.checkEmailUnique(user.getId(), user.getEmail())) {
            return AjaxResult.error("该邮箱已被使用");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUserDto sysUser = loginUser.getUser();
        user.setId(sysUser.getId());
        user.setPassword(null);
        if (userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhone(user.getPhone());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息管理 - 重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePassword")
    public AjaxResult updatePassword(String oldPassword, String newPassword) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        String password = loginUser.getUser().getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
            return AjaxResult.error("修改失败，旧密码错误");
        if (SecurityUtils.matchesPassword(newPassword, password))
            return AjaxResult.error("新旧密码不能相同");
        if (userService.resetUserPassword(loginUser.getUserId(), SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "个人信息管理 - 修改头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getResult()))
                return AjaxResult.error("文件服务异常，请联系管理员");
            String url = fileResult.getResult().getUrl();
            if (userService.updateUserAvatar(SecurityUtils.getUserId(), url) > 0) {
                String oldAvatarUrl = loginUser.getUser().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatarUrl))
                    remoteFileService.delete(oldAvatarUrl);
                AjaxResult ajax = AjaxResult.success();
                ajax.put("avatarUrl", url);
                // 更新缓存 - 用户头像
                loginUser.getUser().setAvatar(url);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
