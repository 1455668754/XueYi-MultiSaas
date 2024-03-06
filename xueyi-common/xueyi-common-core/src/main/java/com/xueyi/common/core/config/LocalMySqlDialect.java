package com.xueyi.common.core.config;

import com.github.pagehelper.dialect.helper.MySqlDialect;
import com.github.pagehelper.parser.CountJSqlParser45;
import com.github.pagehelper.parser.CountSqlParser;
import com.github.pagehelper.parser.OrderByJSqlParser45;
import com.github.pagehelper.parser.OrderBySqlParser;
import com.github.pagehelper.util.ClassUtil;

import java.util.Properties;

/**
 * 解决Mybatis Plus与PageHelper之间的冲突
 * 覆盖父类 {@link com.github.pagehelper.dialect.AbstractDialect} 中的setProperties方法，
 * 将CountJSqlParser45、OrderByJSqlParser45提供的两个类来替换掉Default类
 *
 * @author xueyi
 **/
public class LocalMySqlDialect extends MySqlDialect {

    @Override
    public void setProperties(Properties properties) {
        this.countSqlParser = ClassUtil.newInstance(properties.getProperty("countSqlParser"), CountSqlParser.class, properties, CountJSqlParser45::new);
        this.orderBySqlParser = ClassUtil.newInstance(properties.getProperty("orderBySqlParser"), OrderBySqlParser.class, properties, OrderByJSqlParser45::new);
    }
}
