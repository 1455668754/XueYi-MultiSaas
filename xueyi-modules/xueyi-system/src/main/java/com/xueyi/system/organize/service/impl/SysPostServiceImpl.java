package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.correlate.SysPostCorrelate;
import com.xueyi.system.organize.manager.impl.SysPostManagerImpl;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 岗位管理 服务层处理
 *
 * @author xueyi
 */
@Service
@Isolate
public class SysPostServiceImpl extends BaseServiceImpl<SysPostQuery, SysPostDto, SysPostCorrelate, SysPostManagerImpl> implements ISysPostService {

    /**
     * 用户登录校验 | 根据部门Ids获取归属岗位对象集合
     *
     * @param deptIds 部门Ids
     * @return 岗位对象集合
     */
    @Override
    public List<SysPostDto> selectListByDeptIds(Collection<Long> deptIds) {
        return baseManager.selectListByDeptIds(deptIds);
    }

    /**
     * 新增岗位 | 内部调用
     *
     * @param post 岗位对象
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addInner(SysPostDto post) {
        return super.insert(post);
    }

    /**
     * 查询岗位对象列表 | 数据权限 | 附加数据
     *
     * @param post 岗位对象
     * @return 岗位对象集合
     */
    @Override
    @DataScope(postAlias = "id", mapperScope = {"SysPostMapper"})
    public List<SysPostDto> selectListScope(SysPostQuery post) {
        return super.selectListScope(post);
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param Id   岗位Id
     * @param code 岗位编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkPostCodeUnique(Long Id, String code) {
        return ObjectUtil.isNotNull(baseManager.checkPostCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, code));
    }
}
