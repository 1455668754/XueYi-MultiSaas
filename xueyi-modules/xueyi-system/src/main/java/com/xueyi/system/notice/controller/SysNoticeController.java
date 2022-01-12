package com.xueyi.system.notice.controller;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.service.ISysNoticeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通知公告管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController<SysNoticeDto, ISysNoticeService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "通知公告";
    }
}
