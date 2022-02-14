package com.xueyi.system.organize.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.web.entity.service.impl.SubTreeServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.manager.SysDeptManager;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;
import static com.xueyi.common.core.constant.TenantConstants.SOURCE;

/**
 * 部门管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysDeptServiceImpl extends SubTreeServiceImpl<SysDeptDto, SysDeptManager, SysDeptMapper, SysPostDto, ISysPostService, SysPostMapper> implements ISysDeptService {

    /**
     * 新增部门 | 内部调用
     *
     * @param dept       部门对象
     * @param sourceName 策略源
     * @return 结果
     */
    @Override
    @DS(SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addInner(SysDeptDto dept, String sourceName) {
        return baseManager.insert(dept);
    }

    /**
     * 校验部门编码是否唯一
     *
     * @param Id   部门Id
     * @param code 部门编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkDeptCodeUnique(Long Id, String code) {
        return ObjectUtil.isNotNull(baseManager.checkDeptCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, code));
    }

    /**
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<SysPostDto> postList, SysPostDto post, SysDeptDto dept, Serializable id) {
        Long deptId = ObjectUtil.isNotNull(dept) ? dept.getId() : (Long) id;
        if (ObjectUtil.isNotNull(post))
            post.setDeptId(deptId);
        else
            postList.forEach(sub -> sub.setDeptId(deptId));
    }
}
