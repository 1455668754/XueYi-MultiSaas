package com.xueyi.system.authority.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.authority.controller.base.BSysMenuController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 系统服务 | 权限模块 | 菜单管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/menu")
public class ISysMenuController extends BSysMenuController {

    /**
     * 获取当前节点及其祖籍信息
     */
    @InnerAuth
    @GetMapping("/id")
    public R<SysMenuDto> getInfoInner(@RequestParam Serializable id) {
        return R.ok(baseService.selectById(id));
    }

}
