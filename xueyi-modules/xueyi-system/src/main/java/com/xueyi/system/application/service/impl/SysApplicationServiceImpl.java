package com.xueyi.system.application.service.impl;

import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;
import com.xueyi.system.application.correlate.SysApplicationCorrelate;
import com.xueyi.system.application.manager.ISysApplicationManager;
import com.xueyi.system.application.service.ISysApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统服务 | 应用模块 | 应用管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysApplicationServiceImpl extends BaseServiceImpl<SysApplicationQuery, SysApplicationDto, SysApplicationCorrelate, ISysApplicationManager> implements ISysApplicationService {

    /**
     * 查询登录信息
     *
     * @param appId        应用Id
     * @param enterpriseId 企业Id
     * @return 结果
     */
    @Override
    public SysApplicationDto login(String appId, Long enterpriseId) {
        List<SysApplicationDto> list = baseManager.selectListByLogin(appId, enterpriseId);
        if (CollUtil.isEmpty(list))
            throw new ServiceException("应用不存在");
        else if (list.size() > NumberUtil.One)
            throw new ServiceException("应用匹配到多个结果");
        return list.get(NumberUtil.Zero);
    }

    /**
     * 查询消息应用对象列表 | 数据权限
     *
     * @param application 消息应用对象
     * @return 消息应用对象集合
     */
    @Override
    //@DataScope(userAlias = "createBy", mapperScope = {"TeApplicationMapper"})
    public List<SysApplicationDto> selectListScope(SysApplicationQuery application) {
        return super.selectListScope(application);
    }

    /**
     * 根据应用AppId查询数据对象
     *
     * @param appId 应用AppId
     * @return 数据对象
     */
    @Override
    public SysApplicationDto selectByAppId(String appId) {
        return baseManager.selectByAppId(appId);
    }
}