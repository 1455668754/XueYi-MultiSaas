package com.xueyi.system.file.service.impl;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.file.domain.dto.SysFileDto;
import com.xueyi.system.file.domain.query.SysFileQuery;
import com.xueyi.system.file.manager.ISysFileManager;
import com.xueyi.system.file.service.ISysFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件管理 服务层处理
 *
 * @author xueyi
 */
@Isolate
@Service
public class SysFileServiceImpl extends BaseServiceImpl<SysFileQuery, SysFileDto, ISysFileManager> implements ISysFileService {

    /**
     * 查询文件对象列表 | 数据权限
     *
     * @param file 文件对象
     * @return 文件对象集合
     */
    @Override
    //@DataScope(userAlias = "createBy", mapperScope = {"SysFileMapper"})
    public List<SysFileDto> selectListScope(SysFileQuery file) {
        return baseManager.selectList(file);
    }

    /**
     * 新增文件记录
     *
     * @param file 文件记录对象
     * @return 结果
     */
    @Master
    @Override
    public int insertToMaster(SysFileDto file) {
        return super.insert(file);
    }
}