package com.xueyi.gen.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.gen.domain.po.GenTableColumnPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务字段 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableColumnDto extends GenTableColumnPo {

    private static final long serialVersionUID = 1L;

    /** 字典名称 */
    private String dictName;

    public boolean isPk() {
        return getIsPk();
    }

    public boolean isList() {
        return getIsList();
    }

    public boolean isInsert() {
        return getIsInsert();
    }

    public boolean isView() {
        return getIsView();
    }

    public boolean isEdit() {
        return getIsEdit();
    }

    public boolean isRequired() {
        return getIsRequired();
    }

    public boolean isQuery() {
        return getIsQuery();
    }

    public boolean isImport() {
        return getIsImport();
    }

    public boolean isExport() {
        return getIsExport();
    }

    public boolean isHide() {
        return getIsHide();
    }

    public boolean isCover() {
        return getIsCover();
    }

    public String readConverterExp() {
        String remarks = StrUtil.subBetween(this.getComment(), "（", "）");
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append(startStr).append("=").append(endStr).append(",");
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.getComment();
        }
    }

    public String readNameNoSuffix() {
        return StrUtil.isNotEmpty(this.getComment()) ? this.getComment().replaceAll("(?:\\（)[^\\(\\)]*(?:\\）)", StrUtil.EMPTY) : this.getComment();
    }

}