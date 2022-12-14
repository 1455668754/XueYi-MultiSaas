package com.xueyi.system.file.controller;

import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.file.api.domain.dto.SysFileDto;
import com.xueyi.file.api.domain.query.SysFileQuery;
import com.xueyi.system.file.service.ISysFileService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xueyi.common.core.constant.basic.BaseConstants.NONE_ID;

/**
 * 文件管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/file")
public class SysFileController extends BaseController<SysFileQuery, SysFileDto, ISysFileService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "文件" ;
    }

    /**
     * 文件新增
     */
    @InnerAuth
    @PostMapping
    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    public AjaxResult addInner(@Validated({V_A.class}) @RequestBody SysFileDto file) {
        if (ObjectUtil.isNull(BaseSecurityUtils.getEnterpriseId()))
            SecurityContextHolder.setEnterpriseId(NONE_ID.toString());
        if (ObjectUtil.isNull(BaseSecurityUtils.getSourceName()))
            SecurityContextHolder.setSourceName(TenantConstants.Source.MASTER.getCode());
        return super.add(file);
    }

    /**
     * 文件批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_FILE_DEL)
    @Log(title = "文件管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

}
