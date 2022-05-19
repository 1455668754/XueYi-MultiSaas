package com.xueyi.system.source.manager;

import com.xueyi.system.api.source.domain.Source;

import java.io.Serializable;
import java.util.List;

/**
 * 策略组管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysSourceManager {

    /**
     * 查询源策略组列表
     *
     * @return 策略组集合
     */
    List<Source> selectSourceList();

    /**
     * 根据Id查询源策略组
     *
     * @param id 源策略Id
     * @return 源策略组
     */
    Source selectById(Serializable id);
}
