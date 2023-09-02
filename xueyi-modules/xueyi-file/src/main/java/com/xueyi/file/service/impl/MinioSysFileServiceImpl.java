package com.xueyi.file.service.impl;

import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.file.FileUtil;
import com.xueyi.file.api.domain.SysFile;
import com.xueyi.file.config.MinioConfig;
import com.xueyi.file.service.ISysFileService;
import com.xueyi.file.utils.FileUploadUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Minio 文件存储
 *
 * @author xueyi
 */
@Service
public class MinioSysFileServiceImpl implements ISysFileService {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * Minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     */
    @Override
    public SysFile uploadFile(MultipartFile file) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        InputStream inputStream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        inputStream.close();
        SysFile sysFile = new SysFile();
        sysFile.setUrl(minioConfig.getUrl() + StrUtil.SLASH + minioConfig.getBucketName() + StrUtil.SLASH + fileName);
        sysFile.setPath(minioConfig.getBucketName() + StrUtil.SLASH + fileName);
        sysFile.setSize(file.getSize());
        sysFile.setName(FileUtil.getName(sysFile.getUrl()));
        sysFile.setNick(sysFile.getName());
        return sysFile;
    }

    /**
     * 文件删除接口
     *
     * @param url 文件地址
     * @return 结果
     */
    public Boolean deleteFile(String url) throws Exception {
        return true;
    }
}
