package com.xueyi.system.file.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.file.domain.dto.SysFileDto;
import com.xueyi.system.file.domain.query.SysFileQuery;

/**
 * 系统服务 | 素材模块 | 文件管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysFileManager extends IBaseManager<SysFileQuery, SysFileDto> {

    /**
     * 根据文件Url删除文件
     *
     * @param url 文件路径
     * @return 结果
     */
    int deleteByUrl(String url);
}