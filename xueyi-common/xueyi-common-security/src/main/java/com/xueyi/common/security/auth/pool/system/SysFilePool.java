package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 文件模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysFilePool {

    /** 系统服务 | 素材模块 | 文件管理 - 列表 */
    String SYS_FILE_LIST = "file:file:list";
    /** 系统服务 | 素材模块 | 文件管理 - 详情 */
    String SYS_FILE_SINGLE = "file:file:single";
    /** 系统服务 | 素材模块 | 文件管理 - 新增 */
    String SYS_FILE_ADD = "file:file:add";
    /** 系统服务 | 素材模块 | 文件管理 - 修改 */
    String SYS_FILE_EDIT = "file:file:edit";
    /** 系统服务 | 素材模块 | 文件管理 - 删除 */
    String SYS_FILE_DEL = "file:file:del";

    /** 系统服务 | 素材模块 | 文件分类管理 - 列表 */
    String SYS_FILE_FOLDER_LIST = "file:folder:list";
    /** 系统服务 | 素材模块 | 文件分类管理 - 详情 */
    String SYS_FILE_FOLDER_SINGLE = "file:folder:single";
    /** 系统服务 | 素材模块 | 文件分类管理 - 新增 */
    String SYS_FILE_FOLDER_ADD = "file:folder:add";
    /** 系统服务 | 素材模块 | 文件分类管理 - 修改 */
    String SYS_FILE_FOLDER_EDIT = "file:folder:edit";
    /** 系统服务 | 素材模块 | 文件分类管理 - 删除 */
    String SYS_FILE_FOLDER_DEL = "file:folder:del";
}
