package com.xueyi.system.api.organize.feign.factory;

import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.feign.RemoteDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 部门服务 降级处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RemoteDeptFallbackFactory implements FallbackFactory<RemoteDeptService> {

    @Override
    public RemoteDeptService create(Throwable throwable) {
        log.error("部门服务调用失败:{}", throwable.getMessage());
        return new RemoteDeptService() {
            @Override
            public R<SysDeptDto> addInner(SysDeptDto dept, Long enterpriseId, String sourceName) {
                return R.fail("新增部门失败:" + throwable.getMessage());
            }

            @Override
            public R<SysDeptDto> selectByIdInner(Serializable id) {
                log.error("获取部门信息失败:{}", throwable.getMessage());
                throw new ServiceException("获取部门信息失败");
            }

            @Override
            public R<List<SysDeptDto>> selectListByIdsInner(Collection<? extends Serializable> ids) {
                log.error("获取部门信息失败:{}", throwable.getMessage());
                throw new ServiceException("获取部门信息失败");
            }
        };
    }
}