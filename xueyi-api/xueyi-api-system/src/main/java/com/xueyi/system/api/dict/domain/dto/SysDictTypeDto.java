package com.xueyi.system.api.dict.domain.dto;

import com.xueyi.common.core.annotation.SubRelation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.xueyi.system.api.constant.MergeConstants.DICT_DATA_GROUP;

/**
 * 字典类型 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeDto extends SysDictTypePo {

    private static final long serialVersionUID = 1L;

    @SubRelation(groupName = DICT_DATA_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE_KEY)
    private List<SysMenuDto> dataList;

}
