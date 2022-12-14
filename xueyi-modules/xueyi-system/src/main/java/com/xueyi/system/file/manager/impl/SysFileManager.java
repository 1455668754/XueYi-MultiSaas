package com.xueyi.system.file.manager.impl;

import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.file.api.domain.dto.SysFileDto;
import com.xueyi.file.api.domain.model.SysFileConverter;
import com.xueyi.file.api.domain.po.SysFilePo;
import com.xueyi.file.api.domain.query.SysFileQuery;
import com.xueyi.system.file.manager.ISysFileManager;
import com.xueyi.system.file.mapper.SysFileMapper;
import org.springframework.stereotype.Component;

/**
 * 文件管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysFileManager extends BaseManagerImpl<SysFileQuery, SysFileDto, SysFilePo, SysFileMapper, SysFileConverter> implements ISysFileManager {
}