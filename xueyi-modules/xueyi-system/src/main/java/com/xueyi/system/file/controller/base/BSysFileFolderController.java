package com.xueyi.system.file.controller.base;

import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.file.domain.dto.SysFileFolderDto;
import com.xueyi.system.file.domain.query.SysFileFolderQuery;
import com.xueyi.system.file.service.ISysFileFolderService;

/**
 * 系统服务 | 素材模块 | 文件分类管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysFileFolderController extends TreeController<SysFileFolderQuery, SysFileFolderDto, ISysFileFolderService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "菜单文件分类" ;
    }
}
