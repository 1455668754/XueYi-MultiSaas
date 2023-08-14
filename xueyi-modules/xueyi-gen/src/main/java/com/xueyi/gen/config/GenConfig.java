package com.xueyi.gen.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 代码生成相关配置
 *
 * @author xueyi
 */
@Configuration
@ConfigurationProperties(prefix = "gen", ignoreUnknownFields = false)
public class GenConfig {

    /** 作者 */
    @Getter
    public static String author;

    /** ui路径 */
    @Getter
    public static String uiPath;

    /** 自动去除表前缀，默认是false */
    @Getter
    public static Boolean autoRemovePre;

    /** 数据库映射 */
    @Getter
    private static DataBase dataBase;

    /** 字段配置 */
    @Getter
    private static Operate operate;

    /** 基类配置 */
    @Getter
    private static Entity entity;

    /** 表前缀（与remove-lists对应） */
    @Getter
    private static String[] dictTypeRemove = {};

    /** 表更替配置 */
    @Getter
    public static List<RemoveItem> removeLists;

    /** 字段配置 */
    @Data
    public static class DataBase {

        /** 字符串类型 */
        private String[] typeStr = {};

        /** 文本类型 */
        private String[] typeText = {};

        /** 日期类型 */
        private String[] typeDate = {};

        /** 时间类型 */
        private String[] typeTime = {};

        /** 数字类型 */
        private String[] typeNumber = {};

        /** 长数字类型 */
        private String[] typeLong = {};

        /** 浮点类型 */
        private String[] typeFloat = {};

    }

    /** 字段配置 */
    @Data
    public static class Operate {

        /** 隐藏详情显示 */
        private String[] notView = {};

        /** 隐藏新增显示 */
        private String[] notInsert = {};

        /** 隐藏编辑显示 */
        private String[] notEdit = {};

        /** 隐藏列表显示 */
        private String[] notList = {};

        /** 隐藏查询显示 */
        private String[] notQuery = {};

        /** 隐藏导入显示 */
        private String[] notImport = {};

        /** 隐藏导出显示 */
        private String[] notExport = {};

    }

    /** 基类配置 */
    @Data
    public static class Entity {

        /** 必定隐藏字段（前后端均隐藏） */
        private String[] mustHide = {};

        /** 后端基类 */
        private EntityConfig back = new EntityConfig();

        /** 后端基类 */
        private EntityConfig front = new EntityConfig();

        /** 基类配置类型 */
        @Data
        public static class EntityConfig {

            /** 单表基类 */
            private String[] base = {};

            /** 树表基类 */
            private String[] tree = {};

            /** 租户基类 */
            private String[] tenant = {};

            /** 公共基类 */
            private String[] common = {};

        }

    }

    /** 表更替配置 */
    @Data
    public static class RemoveItem {

        /** 表前缀(类名不会包含表前缀) */
        private String prefix;

        /** 生成包路径 */
        private String packageName;

        /** 生成后端包地址 */
        private String backPackageRoute;

        /** 生成前端包路径 */
        private String frontPackageName;

    }

    public void setAuthor(String author) {
        GenConfig.author = author;
    }

    public void setUiPath(String uiPath) {
        GenConfig.uiPath = uiPath;
    }

    public void setAutoRemovePre(boolean autoRemovePre) {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    public void setDataBase(DataBase dataBase) {
        GenConfig.dataBase = dataBase;
    }

    public void setOperate(Operate operate) {
        GenConfig.operate = operate;
    }

    public void setEntity(Entity entity) {
        GenConfig.entity = entity;
    }

    public void setDictTypeRemove(String[] dictTypeRemove) {
        GenConfig.dictTypeRemove = dictTypeRemove;
    }

    public void setRemoveLists(List<RemoveItem> removeLists) {
        GenConfig.removeLists = removeLists;
    }
}
