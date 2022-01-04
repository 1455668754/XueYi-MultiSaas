package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.SqlConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.domain.organize.dto.SysDeptDto;
import com.xueyi.system.api.domain.organize.dto.SysPostDto;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.utils.vo.OrganizeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysPostManager extends BaseManager<SysPostDto, SysPostMapper> {

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 校验岗位编码是否唯一
     *
     * @param Id   岗位Id
     * @param code 岗位编码
     * @return 岗位对象
     */
    public SysPostDto checkPostCodeUnique(Long Id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysPostDto>query().lambda()
                        .ne(SysPostDto::getId, Id)
                        .eq(SysPostDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 查询部门-岗位组装集合
     *
     * @return 组织对象集合
     */
    public List<OrganizeTree> SelectDeptPostList() {
        List<SysPostDto> postList = baseMapper.selectList(new QueryWrapper<>());
        List<SysDeptDto> deptList = deptMapper.selectList(new QueryWrapper<>());
        List<OrganizeTree> list = new ArrayList<>();
        List<OrganizeTree> post = postList.stream().map(OrganizeTree::new).collect(Collectors.toList());
        List<OrganizeTree> dept = deptList.stream().map(OrganizeTree::new).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(dept)) {
            list.addAll(dept);
        }
        if (CollUtil.isNotEmpty(post)) {
            list.addAll(post);
        }
        return list;
    }
}
