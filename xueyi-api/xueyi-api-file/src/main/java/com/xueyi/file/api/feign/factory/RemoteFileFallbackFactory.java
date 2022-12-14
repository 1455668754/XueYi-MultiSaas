package com.xueyi.file.api.feign.factory;

import com.xueyi.common.core.web.result.R;
import com.xueyi.file.api.domain.dto.SysFileDto;
import com.xueyi.file.api.feign.RemoteFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService> {

    @Override
    public RemoteFileService create(Throwable throwable) {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService() {
            @Override
            public R<SysFileDto> upload(MultipartFile file) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> delete(String url) {
                return R.fail("删除文件失败:" + throwable.getMessage());
            }
        };
    }
}