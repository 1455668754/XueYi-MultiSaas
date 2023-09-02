package com.xueyi.system.file.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.file.domain.correlate.SysFileCorrelate;
import com.xueyi.system.file.domain.dto.SysFileDto;
import com.xueyi.system.file.domain.query.SysFileQuery;
import com.xueyi.system.file.manager.ISysFileManager;
import com.xueyi.system.file.service.ISysFileService;
import org.springframework.stereotype.Service;

/**
 * 系统服务 | 素材模块 | 文件管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysFileServiceImpl extends BaseServiceImpl<SysFileQuery, SysFileDto, SysFileCorrelate, ISysFileManager> implements ISysFileService {

    /**
     * 根据文件Url删除文件
     *
     * @param url 文件路径
     * @return 结果
     */
    @Override
    public int deleteByUrl(String url) {
        return baseManager.deleteByUrl(url);
    }
}