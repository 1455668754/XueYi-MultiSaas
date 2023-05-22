package com.xueyi.system.organize.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.system.organize.controller.base.BSysEnterpriseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/enterprise")
public class ASysEnterpriseController extends BSysEnterpriseController {

    /**
     * 获取当前企业信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return success(SecurityUserUtils.getEnterprise());
    }

//    /**
//     * logo上传
//     */
//    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_LIST)")
//    @RequiresPermissions("system:enterpriseAdmin:edit")
//    @Log(title = "企业Logo修改", businessType = BusinessType.UPDATE)
//    @PostMapping("/changeLogo")
//    public AjaxResult avatar(@RequestParam("logo") MultipartFile file) throws IOException {
//        if (!file.isEmpty()) {
//            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//            R<SysFile> fileResult = remoteFileService.upload(file);
//            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
//                return error("文件服务异常，请稍后再试");
//            }
//            String url = fileResult.getData().getUrl();
//            SysEnterprise enterprise = new SysEnterprise();
//            enterprise.setLogo(url);
//            int rows = refreshCache(enterpriseService.mainUpdateEnterpriseLogo(enterprise));
//            if (rows > 0) {
//                String oldLogoUrl = loginUser.getSysEnterprise().getLogo();
//                if (StringUtils.isNotEmpty(oldLogoUrl)) {
//                    remoteFileService.delete(oldLogoUrl);
//                }
//                AjaxResult ajax = success();
//                ajax.put("imgUrl", url);
//                // 更新缓存用户头像
//                loginUser.getSysEnterprise().setLogo(url);
//                tokenService.setLoginUser(loginUser);
//                return ajax;
//            }
//        }
//        return error("上传图片异常，请稍后再试");
//    }
//
//    /**
//     * 普通信息修改
//     */
//    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_LIST)")
//    @RequiresPermissions("system:enterprise:edit")
//    @Log(title = "企业资料修改", businessType = BusinessType.UPDATE)
//    @PutMapping("/updateEnterprise")
//    public AjaxResult updateEnterprise(@Validated @RequestBody SysEnterprise enterprise) {
//        return toAjax(refreshCache(enterpriseService.mainUpdateEnterpriseMinor(enterprise)));
//    }
//
//    /**
//     * 超管信息修改
//     */
//    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_LIST)")
//    @RequiresPermissions("system:enterpriseAdmin:edit")
//    @Log(title = "企业账号修改", businessType = BusinessType.UPDATE)
//    @PutMapping("/changeEnterpriseName")
//    public AjaxResult changeEnterpriseName(@Validated @RequestBody SysEnterprise enterprise) {
//        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), enterpriseService.mainCheckEnterpriseNameUnique(enterprise))) {
//            return error("修改失败，该企业账号名不可用，请换一个账号名！");
//        }
//        int i = refreshLoginCache(enterpriseService.mainUpdateEnterpriseName(enterprise));
//        Collection<String> keys = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
//        LoginUser mine = tokenService.getLoginUser();
//        //强退当前企业账户所有在线账号
//        for (String key : keys) {
//            LoginUser user = redisService.getCacheObject(key);
//            if (mine.getEnterpriseId().equals(user.getEnterpriseId())) {
//                redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + user.getToken());
//            }
//        }
//        return toAjax(i);
//    }
}
