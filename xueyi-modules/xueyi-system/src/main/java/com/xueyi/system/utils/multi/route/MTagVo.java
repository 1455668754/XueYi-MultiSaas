package com.xueyi.system.utils.multi.route;

import lombok.Data;

/**
 * 菜单标签配置信息
 *
 * @author xueyi
 */
@Data
public class MTagVo {

    /** 为true则显示小圆点 */
    private Boolean dot;

    /** 内容 */
    private String content;

    /** 类型 'error' | 'primary' | 'warn' | 'success' */
    private String type;

}