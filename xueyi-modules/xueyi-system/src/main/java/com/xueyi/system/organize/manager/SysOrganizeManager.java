package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysOrganizeManager {

    @Autowired
    private SysDeptManager deptManager;

    @Autowired
    private SysPostManager postManager;

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    public List<SysOrganizeTree> selectOrganizeScope() {
        List<SysDeptDto> deptList = deptManager.selectList(null);
        List<SysPostDto> postList = postManager.selectList(null);
        return new ArrayList<>(CollUtil.addAll(
                postList.stream().map(SysOrganizeTree::new).collect(Collectors.toList()),
                deptList.stream().map(SysOrganizeTree::new).collect(Collectors.toList())));
    }
}
