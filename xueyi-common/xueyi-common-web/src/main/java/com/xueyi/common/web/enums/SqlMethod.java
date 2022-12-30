package com.xueyi.common.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义操作枚举
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SqlMethod {

    INSERT_BATCH("insertBatch", "批量插入数据", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),
    UPDATE_BATCH("updateBatch", "批量根据ID修改数据", "<script>\n<foreach collection=\"collection\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

}