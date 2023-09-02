package com.xueyi.system.file.manager.impl;

import com.xueyi.system.file.domain.po.SysFileFolderPo;
import com.xueyi.system.file.domain.dto.SysFileFolderDto;
import com.xueyi.system.file.domain.query.SysFileFolderQuery;
import com.xueyi.system.file.domain.model.SysFileFolderConverter;
import com.xueyi.system.file.mapper.SysFileFolderMapper;
import com.xueyi.common.web.entity.manager.impl.TreeManagerImpl;
import com.xueyi.system.file.manager.ISysFileFolderManager;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 素材模块 | 文件分类管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysFileFolderManagerImpl extends TreeManagerImpl<SysFileFolderQuery, SysFileFolderDto, SysFileFolderPo, SysFileFolderMapper, SysFileFolderConverter> implements ISysFileFolderManager {
}