package com.xueyi.file.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.file.api.domain.SysFile;
import com.xueyi.file.api.feign.RemoteFileManageService;
import com.xueyi.file.service.ISysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务 | 文件管理 | 通用 业务处理
 *
 * @author xueyi
 */
@Slf4j
public class BSysFileController {

    @Autowired
    protected ISysFileService fileService;

    @Autowired
    protected RemoteFileManageService remoteFileManageService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件信息
     */
    protected R<SysFile> uploadFile(MultipartFile file) {
        try {
            // 上传并返回访问地址
            SysFile sysFile = fileService.uploadFile(file);
            sysFile.setFolderId(BaseConstants.TOP_ID);
            remoteFileManageService.saveFile(sysFile);
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param url 文件路径
     * @return 结果
     */
    protected R<Boolean> deleteFile(String url) {
        try {
            Boolean result = fileService.deleteFile(url);
            remoteFileManageService.delFile(url);
            return R.ok(result);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return R.fail(e.getMessage());
        }
    }
}