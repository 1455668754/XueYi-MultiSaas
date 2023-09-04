package com.xueyi.job.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.controller.base.BSysJobLogController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务 | 调度日志管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/job/log")
public class ISysJobLogController extends BSysJobLogController {

    /**
     * 新增调度日志
     */
    @PostMapping
    @InnerAuth(isAnonymous = true)
    public R<Boolean> addInner(@RequestBody SysJobLogDto jobLog) {
        baseService.insert(jobLog);
        return R.ok();
    }
}
