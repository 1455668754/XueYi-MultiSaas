package com.xueyi.system.api.organize.feign.factory;

import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.feign.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 用户服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {

            @Override
            public R<SysUserDto> selectByIdInner(Serializable id) {
                log.error("获取用户信息失败:{}", throwable.getMessage());
                throw new ServiceException("获取用户信息失败");
            }

            @Override
            public R<List<SysUserDto>> selectListByIdsInner(Collection<? extends Serializable> ids) {
                log.error("获取用户信息失败:{}", throwable.getMessage());
                throw new ServiceException("获取用户信息失败");
            }

            @Override
            public R<SysUserDto> addInner(SysUserDto user, Long enterpriseId, String sourceName) {
                return R.fail("新增用户失败:" + throwable.getMessage());
            }
        };
    }
}