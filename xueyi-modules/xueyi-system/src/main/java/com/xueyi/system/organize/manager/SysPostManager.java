package com.xueyi.system.organize.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
