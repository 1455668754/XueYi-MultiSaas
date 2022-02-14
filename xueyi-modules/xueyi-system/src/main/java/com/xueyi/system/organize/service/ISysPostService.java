package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.utils.vo.OrganizeTree;

import java.util.List;

/**
 * 岗位管理 服务层
 *
 * @author xueyi
 */
public interface ISysPostService extends IBaseService<SysPostDto> {

    /**
     * 新增岗位 | 内部调用
     *
     * @param post       岗位对象
     * @param sourceName 策略源
     * @return 结果
     */
    int addInner(SysPostDto post, String sourceName);

    /**
     * 校验岗位编码是否唯一
     *
     * @param Id   岗位Id
     * @param code 岗位编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkPostCodeUnique(Long Id, String code);

    /**
     * 构建前端所需要下拉树结构
     *
     * @return 下拉树结构列表
     */
    List<OrganizeTree> buildDeptPostTreeSelect();
}
