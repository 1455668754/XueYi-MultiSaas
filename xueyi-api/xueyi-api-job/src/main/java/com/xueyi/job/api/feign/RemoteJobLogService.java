package com.xueyi.job.api.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.feign.factory.RemoteJobLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 调度日志服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteJobLogService", path = "/inner/job/log", value = ServiceConstants.JOB_SERVICE, fallbackFactory = RemoteJobLogFallbackFactory.class)
public interface RemoteJobLogService {

    /**
     * 保存调度日志
     *
     * @param jobLog       调度日志实体
     * @param enterpriseId 企业Id
     * @param isLessor     企业类型
     * @param sourceName   数据源
     * @return 结果
     */

    @PostMapping(headers = SecurityConstants.FROM_SOURCE_INNER)
    R<Boolean> saveJobLog(@RequestBody SysJobLogDto jobLog, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.IS_LESSOR) String isLessor, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName);
}