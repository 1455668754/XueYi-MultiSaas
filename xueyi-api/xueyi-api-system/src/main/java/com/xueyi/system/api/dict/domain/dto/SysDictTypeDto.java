package com.xueyi.system.api.dict.domain.dto;

import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

import static com.xueyi.system.api.dict.domain.merge.MergeGroup.DICT_DATA_GROUP;

/**
 * 字典类型 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeDto extends SysDictTypePo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 字典数据 */
    @Correlation(groupName = DICT_DATA_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE)
    private List<SysDictDataDto> subList;

}
