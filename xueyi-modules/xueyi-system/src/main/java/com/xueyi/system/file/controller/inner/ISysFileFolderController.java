package com.xueyi.system.file.controller.inner;

import com.xueyi.common.security.annotation.InnerAuth;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xueyi.system.file.controller.base.BSysFileFolderController;

/**
 * 系统服务 | 素材模块 | 文件分类管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/folder")
public class ISysFileFolderController extends BSysFileFolderController {
}
