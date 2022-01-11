package com.xueyi.system.api.log.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.dto.SysOperationLogDto;
import com.xueyi.system.api.log.feign.factory.RemoteLogFallbackFactory;

/**
 * 日志服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteLogService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

    /**
     * 保存系统日志
     *
     * @param operationLog 日志实体
     * @return 结果
     */
    @PostMapping("/operationLog")
    R<Boolean> saveLog(@RequestBody SysOperationLogDto operationLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 保存访问记录
     *
     * @param loginInfo 访问实体
     * @param source    请求来源
     * @return 结果
     */
    @PostMapping("/loginLog")
    R<Boolean> saveLoginInfo(@RequestBody SysLoginLogDto loginInfo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}