package com.xueyi.system.api.organize.domain.query;

import com.xueyi.system.api.organize.domain.po.SysUserPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserQuery extends SysUserPo {

    private static final long serialVersionUID = 1L;

}
