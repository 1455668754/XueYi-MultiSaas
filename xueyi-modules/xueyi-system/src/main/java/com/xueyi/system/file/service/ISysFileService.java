package com.xueyi.system.file.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.file.domain.dto.SysFileDto;
import com.xueyi.system.file.domain.query.SysFileQuery;

/**
 * 文件管理 服务层
 *
 * @author xueyi
 */
public interface ISysFileService extends IBaseService<SysFileQuery, SysFileDto> {

    /**
     * 新增文件记录
     *
     * @param file 文件记录对象
     * @return 结果
     */
    int insertToMaster(SysFileDto file);
}