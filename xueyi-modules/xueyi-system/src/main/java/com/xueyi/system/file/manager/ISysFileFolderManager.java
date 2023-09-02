package com.xueyi.system.file.manager;

import com.xueyi.system.file.domain.dto.SysFileFolderDto;
import com.xueyi.system.file.domain.query.SysFileFolderQuery;
import com.xueyi.common.web.entity.manager.ITreeManager;

/**
 * 系统服务 | 素材模块 | 文件分类管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysFileFolderManager extends ITreeManager<SysFileFolderQuery, SysFileFolderDto> {
}