package com.xueyi.system.notice.controller.base;


import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.NoticeConstants;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.domain.query.SysNoticeQuery;
import com.xueyi.system.notice.service.ISysNoticeService;

/**
 * 系统服务 | 消息模块 | 通知公告管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysNoticeController extends BaseController<SysNoticeQuery, SysNoticeDto, ISysNoticeService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "通知公告";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysNoticeDto notice) {
        // 初始化发送状态
        if (operate.isAdd())
            notice.setStatus(NoticeConstants.NoticeStatus.READY.getCode());
    }
}
