package com.xueyi.system.organize.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.organize.controller.base.BSysDeptController;
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
 * 系统服务 | 组织模块 | 部门管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/dept")
public class ISysDeptController extends BSysDeptController {

    /**
     * 根据Id查询部门信息
     *
     * @param id 部门Id
     * @return 部门信息
     */
    @Override
    @GetMapping("/id")
    public R<SysDeptDto> selectByIdInner(@RequestParam("id") Serializable id) {
        return super.selectByIdInner(id);
    }

    /**
     * 根据Ids查询部门信息集合
     *
     * @param ids 部门Ids
     * @return 部门信息集合
     */
    @Override
    @GetMapping("/list/ids")
    public R<List<SysDeptDto>> selectListByIdsInner(@RequestParam("ids") Collection<? extends Serializable> ids) {
        return super.selectListByIdsInner(ids);
    }

    /**
     * 新增部门
     */
    @PostMapping
    public R<SysDeptDto> addInner(@RequestBody SysDeptDto dept) {
        return baseService.addInner(dept) > 0 ? R.ok(dept) : R.fail();
    }
}
