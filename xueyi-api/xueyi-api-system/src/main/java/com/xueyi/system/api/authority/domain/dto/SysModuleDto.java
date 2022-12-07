package com.xueyi.system.api.authority.domain.dto;

import com.xueyi.common.core.annotation.SubRelation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

import static com.xueyi.system.api.constant.MergeConstants.MODULE_MENU_GROUP;

/**
 * 模块 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysModuleDto extends SysModulePo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 菜单数据 */
    @SubRelation(groupName = MODULE_MENU_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE_KEY)
    private List<SysMenuDto> subList;
}
