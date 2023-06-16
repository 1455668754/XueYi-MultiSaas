package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.TreeServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.organize.correlate.SysDeptCorrelate;
import com.xueyi.system.organize.manager.ISysDeptManager;
import com.xueyi.system.organize.service.ISysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统服务 | 组织模块 | 部门管理 服务层处理
 *
 * @author xueyi
 */
@Service
@Isolate
public class SysDeptServiceImpl extends TreeServiceImpl<SysDeptQuery, SysDeptDto, SysDeptCorrelate, ISysDeptManager> implements ISysDeptService {

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysDeptCorrelate> defaultCorrelate() {
        return new HashMap<>(){{
            put(CorrelateConstants.ServiceType.DELETE, SysDeptCorrelate.BASE_DEL);
        }};
    }

    /**
     * 新增部门 | 内部调用
     *
     * @param dept 部门对象
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addInner(SysDeptDto dept) {
        return super.insert(dept);
    }

    /**
     * 查询部门对象列表 | 数据权限 | 附加数据
     *
     * @param dept 部门对象
     * @return 部门对象集合
     */
    @Override
    @DataScope(deptAlias = "id", mapperScope = {"SysDeptMapper"})
    public List<SysDeptDto> selectListScope(SysDeptQuery dept) {
        return super.selectListScope(dept);
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

}
