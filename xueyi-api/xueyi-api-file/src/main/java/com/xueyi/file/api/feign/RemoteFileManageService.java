package com.xueyi.file.api.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.file.api.domain.dto.SysFileDto;
import com.xueyi.file.api.feign.factory.RemoteFileManageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 文件管理服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteFileManageService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteFileManageFallbackFactory.class)
public interface RemoteFileManageService {

    /**
     * 保存文件记录
     *
     * @param file         文件实体
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/file")
    R<Boolean> saveFileLog(@RequestBody SysFileDto file, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}