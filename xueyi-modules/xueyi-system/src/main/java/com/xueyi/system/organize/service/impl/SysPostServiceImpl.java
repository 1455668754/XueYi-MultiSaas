package com.xueyi.system.organize.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.domain.organize.dto.SysPostDto;
import com.xueyi.system.organize.manager.SysPostManager;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.service.ISysPostService;
import com.xueyi.system.utils.vo.OrganizeTree;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysPostServiceImpl extends BaseServiceImpl<SysPostDto, SysPostManager, SysPostMapper> implements ISysPostService {

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

    /**
     * 构建前端所需要下拉树结构
     *
     * @return 下拉树结构列表
     */
    @Override
    public List<OrganizeTree> buildDeptPostTreeSelect() {
        List<OrganizeTree> organizeList = baseManager.SelectDeptPostList();
        return TreeUtils.buildTree(organizeList);
    }
}
