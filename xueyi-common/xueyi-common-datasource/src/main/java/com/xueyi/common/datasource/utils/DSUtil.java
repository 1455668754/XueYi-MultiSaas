package com.xueyi.common.datasource.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.xueyi.common.cache.utils.SourceUtil;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 源管理工具类
 *
 * @author xueyi
 */
@Slf4j
public class DSUtil {

    /**
     * 数据源动态加载
     *
     * @param sourceName 数据源编码
     */
    public static String loadDs(String sourceName) {
        if (StrUtil.isEmpty(sourceName)) {
            throw new UtilException("数据源不存在！");
        } else if (checkHasDs(sourceName)) {
            return sourceName;
        }
        TeSourceDto source = SourceUtil.getTeSourceCache(sourceName);
        if (ObjectUtil.isNull(source)) {
            throw new UtilException("数据源缓存不存在！");
        }
        addDs(source);
        return sourceName;
    }

    /**
     * 添加一个数据源到数据源库中
     *
     * @param source 数据源对象
     */
    public static void addDs(TeSourceDto source) {
        try {
            DefaultDataSourceCreator dataSourceCreator = SpringUtil.getBean(DefaultDataSourceCreator.class);
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            dataSourceProperty.setDriverClassName(source.getDriverClassName());
            dataSourceProperty.setUrl(source.getUrlPrepend() + source.getUrlAppend());
            dataSourceProperty.setUsername(source.getUserName());
            dataSourceProperty.setPassword(source.getPassword());
            DataSource dataSource = SpringUtil.getBean(DataSource.class);
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
            ds.addDataSource(source.getSlave(), dataSource);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UtilException("数据源添加失败!");
        }
    }

    /**
     * 从数据源库中删除一个数据源
     *
     * @param slave 数据源编码
     */
    public static void delDs(String slave) {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtil.getBean(DataSource.class);
            ds.removeDataSource(slave);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UtilException("数据源删除失败!");
        }
    }

    /**
     * 获取当前数据源库中所有数据源
     */
    public static void getDs() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtil.getBean(DataSource.class);
        ds.getDataSources().keySet().forEach(System.out::println);
    }

    /**
     * 是否存在指定数据源
     */
    public static boolean checkHasDs(String slave) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtil.getBean(DataSource.class);
        return ds.getDataSources().containsKey(slave);
    }

    /**
     * 获取当前线程数据源名称
     */
    public static String getNowDsName() {
        return DynamicDataSourceContextHolder.peek();
    }

    /**
     * 测试数据源是否可连接
     *
     * @param source 数据源对象
     */
    public static void testDs(TeSourceDto source) {
        try {
            Class.forName(source.getDriverClassName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UtilException("数据源驱动加载失败，请检查驱动信息！");
        }
        try {
            Connection dbConn = DriverManager.getConnection(source.getUrlPrepend() + source.getUrlAppend(), source.getUserName(), source.getPassword());
            dbConn.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UtilException("数据源连接失败，请检查连接信息！");
        }
    }

    /**
     * 测试数据源是否为可连接子库
     *
     * @param source 数据源对象
     */
    public static void testSlaveDs(TeSourceDto source, List<String> needTable) {
        try {
            Class.forName(source.getDriverClassName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("数据源驱动加载失败");
        }
        try {
            Connection dbConn = DriverManager.getConnection(source.getUrlPrepend() + source.getUrlAppend(), source.getUserName(), source.getPassword());
            PreparedStatement statement = dbConn.prepareStatement("select table_name from information_schema.tables where table_schema = (select database())");
            ResultSet resultSet = statement.executeQuery();
            Set<String> tableNameList = new HashSet<>();
            while (resultSet.next()) {
                tableNameList.add(resultSet.getString("table_name"));
            }
            Set<String> slaveTables = new HashSet<>(needTable);
            slaveTables.removeAll(tableNameList);
            if (CollUtil.isNotEmpty(slaveTables)) {
                throw new ServiceException("请连接包含子库数据表信息的数据源！");
            }
            dbConn.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("数据源连接失败，请检查连接信息！");
        }
    }
}