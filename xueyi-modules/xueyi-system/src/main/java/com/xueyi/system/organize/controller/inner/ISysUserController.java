package com.xueyi.system.organize.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.organize.controller.base.BSysUserController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 用户管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/user")
public class ISysUserController extends BSysUserController {

    /**
     * 根据Id查询用户信息
     *
     * @param id 用户Id
     * @return 用户信息
     */
    @Override
    @GetMapping("/id")
    public R<SysUserDto> selectByIdInner(@RequestParam Serializable id) {
        return super.selectByIdInner(id);
    }

    /**
     * 根据Ids查询用户信息集合
     *
     * @param ids 用户Ids
     * @return 用户信息集合
     */
    @Override
    @GetMapping("/list/ids")
    public R<List<SysUserDto>> selectListByIdsInner(@RequestParam Collection<? extends Serializable> ids) {
        return super.selectListByIdsInner(ids);
    }

    /**
     * 新增用户 | 内部调用
     */
    @PostMapping("/add")
    public R<SysUserDto> addInner(@RequestBody SysUserDto user) {
        return baseService.addInner(user) > 0 ? R.ok(user) : R.fail();
    }
}
