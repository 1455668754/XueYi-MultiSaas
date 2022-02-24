package com.xueyi.system.api.file.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.file.domain.SysFile;
import com.xueyi.system.api.file.feign.factory.RemoteFileFallbackFactory;

/**
 * 文件服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteFileService", value = ServiceConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService {

    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 上传文件
     *
     * @param url 文件地址
     * @return 结果
     */
    @PostMapping(value = "/delete")
    R<Boolean> delete(@RequestParam(value = "url") String url);
}
