package com.xueyi.system.authority.controller.inner;

import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.authority.controller.base.BSysAuthGroupController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 权限模块 | 企业权限组管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/authGroup")
public class ISysAuthGroupController extends BSysAuthGroupController {
}
