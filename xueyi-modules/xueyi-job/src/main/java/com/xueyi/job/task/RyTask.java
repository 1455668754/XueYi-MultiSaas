package com.xueyi.job.task;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author xueyi
 */
@Component("ryTask")
public class RyTask {

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i, Long enterpriseId, String isLessor, String sourceName) {
        System.out.println(StrUtil.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}, 企业Id{}, 租户类型{}, 源{}", s, b, l, d, i, enterpriseId, isLessor, sourceName));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }
}
