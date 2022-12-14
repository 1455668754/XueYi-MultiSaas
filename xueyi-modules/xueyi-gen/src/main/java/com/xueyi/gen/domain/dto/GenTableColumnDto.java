package com.xueyi.gen.domain.dto;

import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.gen.domain.po.GenTableColumnPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 业务字段 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableColumnDto extends GenTableColumnPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 字段判别 - Po显示 */
    private Boolean isPo;

    /** 字段判别 - 与表一致 */
    private Boolean isDivideTable;

    /** 字典名称 */
    private String dictName;

    public String readConverterExp() {
        String remarks = StrUtil.subBetween(this.getComment(), "（", "）");
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StrUtil.isNotEmpty(value)) {
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