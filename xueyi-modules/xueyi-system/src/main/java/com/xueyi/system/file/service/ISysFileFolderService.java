package com.xueyi.system.file.service;

import com.xueyi.system.file.domain.query.SysFileFolderQuery;
import com.xueyi.system.file.domain.dto.SysFileFolderDto;
import com.xueyi.common.web.entity.service.ITreeService;

/**
 * 系统服务 | 素材模块 | 文件分类管理 服务层
 *
 * @author xueyi
 */
public interface ISysFileFolderService extends ITreeService<SysFileFolderQuery, SysFileFolderDto> {
}