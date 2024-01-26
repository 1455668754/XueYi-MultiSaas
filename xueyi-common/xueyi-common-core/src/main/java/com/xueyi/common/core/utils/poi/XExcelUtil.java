package com.xueyi.common.core.utils.poi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.DateUtil;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.common.core.utils.file.FileTypeUtil;
import com.xueyi.common.core.utils.file.ImageUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Excel相关处理
 *
 * @author xueyi
 */
@Slf4j
@Data
public class XExcelUtil {

    public static final String FORMULA_REGEX_STR = "=|-|\\+|@";

    public static final String[] FORMULA_STR = {"=", "-", "+", "@"};

    /** 数字格式 */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /** Excel配置 */
    private ExcelInfo config;

    public XExcelUtil(String configStr, Class<?>... clazz) {
        if (ObjectUtil.isNull(configStr)) {
            throw new UtilException("excel配置不存在！");
        } else if (ArrayUtil.isEmpty(clazz)) {
            throw new UtilException("实体对象Class不存在！");
        }
        setConfig(new ExcelInfo(configStr, clazz));
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public <T> List<T> importExcel(InputStream is) {
        List<T> list;
        try {
            list = importExcel(StrUtil.EMPTY, is);
        } catch (Exception e) {
            log.error("导入Excel异常{}", e.getMessage());
            throw new UtilException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return list;
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is        输入流
     * @return 转换后集合
     */
    @SneakyThrows
    public <T> List<T> importExcel(String sheetName, InputStream is) {
        return this.config.importExcel(sheetName, is);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param response 返回数据
     * @param list     导出数据集合
     */
    public void exportExcel(HttpServletResponse response, List<?> list) {
        this.config.initList(list);
        this.config.exportExcel(response);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param response 返回数据
     * @param list     导出数据集合
     */
    public void exportMoreSheetExcel(HttpServletResponse response, Map<String, List<?>> list) {
        this.config.initLists(list);
        this.config.exportExcel(response);
    }

    /**
     * 导出Excel模板
     */
    public void importTemplateExcel(HttpServletResponse response) {
        this.config.exportExcel(response);
    }

    /**
     * excel配置信息对象
     */
    @Data
    @NoArgsConstructor
    public static class ExcelInfo {

        /** 工作表配置信息集合 */
        private List<SheetInfo<?>> sheetInfoList;

        /** 自动计算 · 工作薄对象 */
        private Workbook wb;

        /** 自动计算 · 单个sheet */
        private Boolean isSingle = Boolean.TRUE;

        public ExcelInfo(String configStr, Class<?>[] clazz) {
            this.sheetInfoList = new ArrayList<>();
            // 判定集合 or 对象
            if (JSON.isValidArray(configStr)) {
                JSONArray arr = JSON.parseArray(configStr);
                int arrSize = arr.size();
                if (!NumberUtil.equals(arrSize, clazz.length)) {
                    throw new UtilException("excel配置需要{}个实体Class，实际仅导入{}个Class！", arrSize, clazz.length);
                }
                this.isSingle = Boolean.FALSE;
                for (int i = 0; i < arrSize; i++) {
                    this.sheetInfoList.add(new SheetInfo<>(arr.getJSONObject(i), clazz[i]));
                }
            } else {
                JSONObject json = JSON.parseObject(configStr);
                if (!NumberUtil.equals(NumberUtil.One, clazz.length)) {
                    throw new UtilException("excel配置需要{}个实体Class，实际仅导入{}个Class！", NumberUtil.One, clazz.length);
                }
                this.sheetInfoList.add(new SheetInfo<>(json, clazz[NumberUtil.Zero]));
            }
        }

        /**
         * 初始化Sheet数据集合
         *
         * @param list 数据集合
         */
        private void initList(List<?> list) {
            this.sheetInfoList.get(0).initSheetList(list);
        }

        /**
         * 初始化Sheet数据集合
         *
         * @param list 数据集合
         */
        private void initLists(Map<String, List<?>> list) {
            if (CollUtil.isNotEmpty(list)) {
                AtomicInteger index = new AtomicInteger(0);
                list.values().forEach(item -> this.sheetInfoList.get(index.getAndIncrement()).initSheetList(item));
            }
        }

        /**
         * 对excel表单指定表格索引名转换成list
         *
         * @param sheetName 表格索引名
         * @param is        输入流
         * @return 转换后集合
         */
        @SneakyThrows
        private <T> List<T> importExcel(String sheetName, InputStream is) {
            this.wb = WorkbookFactory.create(is);
            List<T> list;
            // 如果指定sheet名,则取指定sheet中的内容 否则默认指向第1个sheet
            Sheet sheet = StrUtil.isNotBlank(sheetName) ? this.wb.getSheet(sheetName) : this.wb.getSheetAt(0);
            if (ObjectUtil.isNull(sheet)) {
                throw new IOException("文件sheet不存在");
            }
            int index = this.wb.getSheetIndex(sheet);
            try {
                list = (List<T>) this.sheetInfoList.get(index).importExcel(sheet);
            } catch (Exception e) {
                throw new UtilException(e);
            }
            return list;
        }

        /**
         * 创建一个工作簿
         */
        private void createWorkbook() {
            this.wb = new SXSSFWorkbook(500);
        }

        /**
         * 对list数据源将其里面的数据导入到excel表单
         */
        private void exportExcel(HttpServletResponse response) {
            try {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                createWorkbook();
                AtomicInteger index = new AtomicInteger(NumberUtil.Zero);
                this.sheetInfoList.forEach(sheetInfo -> {
                    sheetInfo.setType(ExcelType.EXPORT);
                    sheetInfo.writeSheet(this.wb, index);
                });
                wb.write(response.getOutputStream());
            } catch (Exception e) {
                log.error("导出Excel异常{}", e.getMessage());
            } finally {
                IOUtils.closeQuietly(wb);
            }
        }

        /**
         * 工作表配置信息对象
         */
        @Data
        @NoArgsConstructor
        public static class SheetInfo<C> {

            /** 工作表名称 */
            private String sheetName;

            /** 标题 */
            private String title;

            /** 标题 */
            private Integer titleNum = NumberUtil.Zero;

            /** 实体对象 */
            private Class<C> clazz;

            /** 自动计算 · 工作表对象 */
            private Sheet sheet;

            /** 自动计算 · 样式列表 */
            private Map<String, CellStyle> styles;

            /** 自动计算 · 最大高度 */
            private short maxHeight = NumberUtil.Zero;

            /** 自动计算 · 当前行号 */
            private int rowNum;

            /** 自动计算 · 合并后最后行数 */
            private int subMergedLastRowNum = 0;

            /** 自动计算 · 合并后开始行数 */
            private int subMergedFirstRowNum = 1;

            /** 自动计算 · 导出类型（EXPORT:导出数据；IMPORT：导入模板） */
            private ExcelType type;

            /** 自动分页 | 超过sheet最大行数后自动新增sheet页 */
            private Boolean isCollate = Boolean.TRUE;

            /** sheet最大行数 */
            private Integer maxSheetSize = 65536;

            /** 自动计算 · 统计列表 */
            private Map<Integer, Double> statistics = new HashMap<>();

            /** excel字段信息集合 */
            private List<FieldInfo> fieldList;

            /** 导入导出数据列表 */
            private List<C> list = new ArrayList<>();

            /**
             * 初始化工作表配置
             *
             * @param json  excel配置JSON
             * @param clazz 实体对象Class
             */
            public SheetInfo(JSONObject json, Class<C> clazz) {
                this.clazz = clazz;
                Field[] fields = SheetInfo.class.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    try {
                        field.setAccessible(Boolean.TRUE);
                        String value = json.getString(fieldName);
                        if (StrUtil.isNotBlank(value)) {
                            // 常规类型
                            if (ClassUtil.isSimpleType(field.getType())) {
                                field.set(this, ConvertUtil.convert(field.getType(), value));
                            }
                            // 是否为集合类型
                            else if (ClassUtil.isCollection(field.getType())) {
                                Type type = field.getGenericType();
                                ParameterizedType parameterizedType = (ParameterizedType) type;
                                if (parameterizedType.getActualTypeArguments()[0] instanceof Class<?> collectionType) {
                                    if (ClassUtil.equals(FieldInfo.class, collectionType)) {
                                        JSONArray array = json.getJSONArray(fieldName);
                                        List<FieldInfo> fieldList = array.toList(JSONObject.class).stream().map(FieldInfo::new).sorted(Comparator.comparing(FieldInfo::getSort)).collect(Collectors.toList());
                                        field.set(this, fieldList);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("excel配置转换异常，{}字段存在不匹配，错误原因：{}", fieldName, e.getMessage());
                    }
                }
                // 提取所有参与excel配置的字段
                this.fieldList = upgradeLevel(this.fieldList, this.clazz);
                // 计算行最大高度
                this.maxHeight = getRowHeight();
            }

            /**
             * 初始化Sheet数据集合
             *
             * @param list 数据集合
             */
            private void initSheetList(List<?> list) {
                if (CollUtil.isNull(list)) {
                    list = new ArrayList<>();
                }
                this.list = (List<C>) list;
            }

            /**
             * 提取所有参与excel配置的字段
             *
             * @param fieldList excel字段信息集合
             * @param clazz     Class对象
             * @return 参与excel配置字段信息集合
             */
            private List<FieldInfo> upgradeLevel(List<FieldInfo> fieldList, Class<?> clazz) {
                List<FieldInfo> upLevelFieldList = new ArrayList<>();
                if (CollUtil.isNotEmpty(fieldList)) {
                    fieldList.forEach(fieldInfo -> {
                        if (CollUtil.isEmpty(fieldInfo.getFields())) {
                            fieldInfo.setFields(new ArrayList<>());
                        }
                        Field field;
                        try {
                            field = ReflectUtil.getField(clazz, fieldInfo.getField());
                            if (ObjectUtil.isNull(field)) {
                                throw new UtilException("字段{}在class中不存在", fieldInfo.getField());
                            }
                        } catch (Exception e) {
                            throw new UtilException("字段{}在class中不存在", fieldInfo.getField());
                        }
                        field.setAccessible(true);
                        fieldInfo.getFields().add(field);
                        if (CollUtil.isNotEmpty(fieldInfo.getChildren())) {
                            fieldInfo.getChildren().forEach(item -> item.setFields(new ArrayList<>(fieldInfo.getFields())));
                            if (!(ClassUtil.isNormalClass(field.getType()) && ClassUtil.isNotSimpleType(field.getType()))) {
                                throw new UtilException("字段{}配置存在子字段，但对应的属性非对象类型", fieldInfo.getField());
                            }
                            upLevelFieldList.addAll(upgradeLevel(fieldInfo.getChildren(), field.getType()));
                        } else {
                            upLevelFieldList.add(fieldInfo);
                        }
                    });
                }
                return upLevelFieldList.stream().peek(item -> item.setChildren(null))
                        .sorted(Comparator.comparing(FieldInfo::getSort)).collect(Collectors.toList());
            }

            /**
             * 根据注解获取最大行高
             *
             * @return 最大高度
             */
            private short getRowHeight() {
                double maxHeight = 0;
                for (FieldInfo field : this.fieldList) {
                    maxHeight = Math.max(maxHeight, field.getHeight());
                }
                return (short) (maxHeight * 20);
            }

            /**
             * 创建工作表
             *
             * @param wb    工作薄对象
             * @param index 序号
             */
            private void createSheet(Workbook wb, int index, String sheetName) {
                this.sheet = wb.createSheet();
                wb.setSheetName(index, StrUtil.format("{}{}", sheetName));
                createStyles(wb);
                createTitle();
            }

            /**
             * 创建表格样式
             *
             * @param wb 工作薄对象
             */
            private void createStyles(Workbook wb) {
                // 写入各条记录,每条记录对应excel表中的一行
                Map<String, CellStyle> styles = new HashMap<>();
                CellStyle style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                Font titleFont = wb.createFont();
                titleFont.setFontName("Arial");
                titleFont.setFontHeightInPoints((short) 16);
                titleFont.setBold(true);
                style.setFont(titleFont);
                styles.put("title", style);

                style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setBorderRight(BorderStyle.THIN);
                style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                style.setBorderLeft(BorderStyle.THIN);
                style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                style.setBorderTop(BorderStyle.THIN);
                style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                style.setBorderBottom(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                Font dataFont = wb.createFont();
                dataFont.setFontName("Arial");
                dataFont.setFontHeightInPoints((short) 10);
                style.setFont(dataFont);
                styles.put("data", style);

                style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                Font totalFont = wb.createFont();
                totalFont.setFontName("Arial");
                totalFont.setFontHeightInPoints((short) 10);
                style.setFont(totalFont);
                styles.put("total", style);

                annotationHeaderStyles(wb, styles, this.fieldList);
                annotationDataStyles(wb, styles, this.fieldList);
                this.styles = styles;
            }

            /**
             * 根据Excel注解创建表格头样式
             *
             * @param wb        工作薄对象
             * @param styles    样式列表
             * @param fieldList excel字段信息集合
             */
            private void annotationHeaderStyles(Workbook wb, Map<String, CellStyle> styles, List<FieldInfo> fieldList) {
                for (FieldInfo field : fieldList) {
                    String key = StrUtil.format("header_{}_{}", field.getHeaderColor(), field.getHeaderBackgroundColor());
                    if (!styles.containsKey(key)) {
                        CellStyle style = wb.createCellStyle();
                        style.cloneStyleFrom(styles.get("data"));
                        style.setAlignment(HorizontalAlignment.CENTER);
                        style.setVerticalAlignment(VerticalAlignment.CENTER);
                        style.setFillForegroundColor(field.getHeaderBackgroundColor().index);
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        Font headerFont = wb.createFont();
                        headerFont.setFontName("Arial");
                        headerFont.setFontHeightInPoints((short) 10);
                        headerFont.setBold(true);
                        headerFont.setColor(field.getHeaderColor().index);
                        style.setFont(headerFont);
                        styles.put(key, style);
                    }
                }
            }

            /**
             * 根据Excel注解创建表格列样式
             *
             * @param wb        工作薄对象
             * @param styles    样式列表
             * @param fieldList excel字段信息集合
             */
            private void annotationDataStyles(Workbook wb, Map<String, CellStyle> styles, List<FieldInfo> fieldList) {
                for (FieldInfo field : fieldList) {
                    String key = StrUtil.format("data_{}_{}_{}", field.getAlign(), field.getColor(), field.getBackgroundColor());
                    if (!styles.containsKey(key)) {
                        CellStyle style = wb.createCellStyle();
                        style.setAlignment(field.getAlign());
                        style.setBorderRight(BorderStyle.THIN);
                        style.setVerticalAlignment(VerticalAlignment.CENTER);
                        style.setBorderLeft(BorderStyle.THIN);
                        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                        style.setBorderTop(BorderStyle.THIN);
                        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                        style.setBorderBottom(BorderStyle.THIN);
                        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
                        style.setFillForegroundColor(field.getBackgroundColor().getIndex());
                        Font dataFont = wb.createFont();
                        dataFont.setFontName("Arial");
                        dataFont.setFontHeightInPoints((short) 10);
                        dataFont.setColor(field.getColor().index);
                        style.setFont(dataFont);
                        styles.put(key, style);
                    }
                }
            }

            /**
             * 创建excel第一行标题
             */
            private void createTitle() {
                if (StrUtil.isNotBlank(this.title)) {
                    this.subMergedFirstRowNum++;
                    this.subMergedLastRowNum++;
                    int titleLastCol = this.fieldList.size() - 1;
                    Row titleRow = this.sheet.createRow(this.rowNum == 0 ? this.rowNum++ : 0);
                    titleRow.setHeightInPoints(30);
                    Cell titleCell = titleRow.createCell(0);
                    titleCell.setCellStyle(this.styles.get("title"));
                    titleCell.setCellValue(this.title);
                    this.sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), titleLastCol));
                }
            }

            /**
             * 创建写入数据到Sheet
             *
             * @param wb              工作薄对象
             * @param startSheetIndex sheet序号
             */
            private void writeSheet(Workbook wb, AtomicInteger startSheetIndex) {
                // 取出一共有多少个sheet
                int sheetNo = Math.max(1, this.isCollate ? (int) Math.ceil(this.list.size() * 1.0 / this.maxSheetSize) : 1);
                for (int index = 0; index < sheetNo; index++) {
                    createSheet(wb, startSheetIndex.getAndIncrement(), this.sheetName + index);
                    // 产生一行
                    Row row = this.sheet.createRow(this.rowNum);
                    // 写入各个字段的列头名称
                    AtomicInteger column = new AtomicInteger(0);

                    // 写入各个字段的列头名称
                    for (FieldInfo fieldInfo : this.fieldList) {
                        createHeadCell(wb, startSheetIndex, fieldInfo, row, column.getAndIncrement());
                    }
                    if (ObjectUtil.equals(ExcelType.EXPORT, this.type)) {
                        fillExcelData(wb, index, row);
                        addStatisticsRow();
                    }
                }
            }

            /**
             * 创建单元格
             *
             * @param wb              工作薄对象
             * @param startSheetIndex sheet序号
             * @param fieldInfo       字段信息对象
             * @param row             行
             * @param column          列序号
             * @return 单元格信息
             */
            private Cell createHeadCell(Workbook wb, AtomicInteger startSheetIndex, FieldInfo fieldInfo, Row row, int column) {
                // 创建列
                Cell cell = row.createCell(column);
                // 写入列信息
                cell.setCellValue(fieldInfo.getName());
                setDataValidation(wb, startSheetIndex, fieldInfo, row, column);
                cell.setCellStyle(this.styles.get(StrUtil.format("header_{}_{}", fieldInfo.getHeaderColor(), fieldInfo.getHeaderBackgroundColor())));
                return cell;
            }

            /**
             * 创建表格样式
             *
             * @param wb              工作薄对象
             * @param startSheetIndex sheet序号
             * @param fieldInfo       字段信息对象
             * @param row             行
             * @param column          列序号
             */
            private void setDataValidation(Workbook wb, AtomicInteger startSheetIndex, FieldInfo fieldInfo, Row row, int column) {
                if (fieldInfo.getName().contains("注：")) {
                    sheet.setColumnWidth(column, 6000);
                } else {
                    // 设置列宽
                    sheet.setColumnWidth(column, (int) ((fieldInfo.getWidth() + 0.72) * 256));
                }
                if (StrUtil.isNotBlank(fieldInfo.getPrompt()) || CollUtil.isNotEmpty(fieldInfo.getCombo())) {
                    String[] textList = (CollUtil.isNotEmpty(fieldInfo.getCombo()) ? fieldInfo.getCombo() : new ArrayList<FieldInfo.ModelInfo>()).stream()
                            .map(FieldInfo.ModelInfo::getLabel).filter(StrUtil::isNotBlank).toArray(String[]::new);
                    // 提示信息或只能选择不能输入的列内容.
                    if (textList.length > 15 || (StrUtil.isNotBlank(fieldInfo.getPrompt()) && fieldInfo.getPrompt().length() > 255)) {
                        // 如果下拉数大于15或字符串长度大于255，则使用一个新sheet存储，避免生成的模板下拉值获取不到
                        setXSSFValidationWithHidden(wb, startSheetIndex, sheet, textList, fieldInfo.getPrompt(), 1, 100, column, column);
                    } else {
                        // 提示信息或只能选择不能输入的列内容.
                        setPromptOrValidation(sheet, textList, fieldInfo.getPrompt(), 1, 100, column, column);
                    }
                }
            }

            /**
             * 设置某些列的值只能输入预制的数据,显示下拉框（兼容超出一定数量的下拉框）.
             *
             * @param wb              工作薄对象
             * @param startSheetIndex sheet序号
             * @param sheet           要设置的sheet.
             * @param textList        下拉框显示的内容
             * @param promptContent   提示内容
             * @param firstRow        开始行
             * @param endRow          结束行
             * @param firstCol        开始列
             * @param endCol          结束列
             */
            private void setXSSFValidationWithHidden(Workbook wb, AtomicInteger startSheetIndex, Sheet sheet, String[] textList, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
                String hideSheetName = "combo_" + firstCol + "_" + endCol;
                // 用于存储 下拉菜单数据
                Sheet hideSheet = wb.createSheet(hideSheetName);
                startSheetIndex.getAndIncrement();
                for (int i = 0; i < textList.length; i++) {
                    hideSheet.createRow(i).createCell(0).setCellValue(textList[i]);
                }
                // 创建名称，可被其他单元格引用
                Name name = wb.createName();
                name.setNameName(hideSheetName + "_data");
                name.setRefersToFormula(hideSheetName + "!$A$1:$A$" + textList.length);
                DataValidationHelper helper = sheet.getDataValidationHelper();
                // 加载下拉列表内容
                DataValidationConstraint constraint = helper.createFormulaListConstraint(hideSheetName + "_data");
                // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
                CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
                // 数据有效性对象
                DataValidation dataValidation = helper.createValidation(constraint, regions);
                if (StringUtils.isNotEmpty(promptContent)) {
                    // 如果设置了提示信息则鼠标放上去提示
                    dataValidation.createPromptBox("", promptContent);
                    dataValidation.setShowPromptBox(true);
                }
                // 处理Excel兼容性问题
                if (dataValidation instanceof XSSFDataValidation) {
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.setShowErrorBox(true);
                } else {
                    dataValidation.setSuppressDropDownArrow(false);
                }

                sheet.addValidationData(dataValidation);
                // 设置hiddenSheet隐藏
                wb.setSheetHidden(wb.getSheetIndex(hideSheet), true);
            }

            /**
             * 设置 POI XSSFSheet 单元格提示或选择框
             *
             * @param sheet         表单
             * @param textList      下拉框显示的内容
             * @param promptContent 提示内容
             * @param firstRow      开始行
             * @param endRow        结束行
             * @param firstCol      开始列
             * @param endCol        结束列
             */
            private void setPromptOrValidation(Sheet sheet, String[] textList, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
                DataValidationHelper helper = sheet.getDataValidationHelper();
                DataValidationConstraint constraint = textList.length > 0 ? helper.createExplicitListConstraint(textList) : helper.createCustomConstraint("DD1");
                CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
                DataValidation dataValidation = helper.createValidation(constraint, regions);
                if (StrUtil.isNotEmpty(promptContent)) {
                    // 如果设置了提示信息则鼠标放上去提示
                    dataValidation.createPromptBox("", promptContent);
                    dataValidation.setShowPromptBox(true);
                }
                // 处理Excel兼容性问题
                if (dataValidation instanceof XSSFDataValidation) {
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.setShowErrorBox(true);
                } else {
                    dataValidation.setSuppressDropDownArrow(false);
                }
                sheet.addValidationData(dataValidation);
            }

            /**
             * 填充excel数据
             *
             * @param wb    工作薄对象
             * @param index 序号
             * @param row   单元格行
             */
            private void fillExcelData(Workbook wb, int index, Row row) {
                int startNo = index * this.maxSheetSize;
                int endNo = Math.min(startNo + this.maxSheetSize, this.list.size());
                for (int i = startNo; i < endNo; i++) {
                    int rowNo = i + 1 + this.rowNum - startNo;
                    row = this.sheet.createRow(rowNo);
                    // 得到导出对象.
                    C vo = this.list.get(i);
                    AtomicInteger column = new AtomicInteger(0);
                    for (FieldInfo fieldInfo : this.fieldList) {
                        addCell(wb, fieldInfo, row, vo, column.getAndIncrement());
                    }
                }
            }

            /**
             * 添加单元格
             *
             * @param wb        工作薄对象
             * @param fieldInfo 字段信息对象
             * @param row       单元格行
             * @param vo        数据对象
             * @param column    列
             */
            private Cell addCell(Workbook wb, FieldInfo fieldInfo, Row row, C vo, int column) {
                Cell cell = null;
                try {
                    // 设置行高
                    row.setHeight(this.maxHeight);
                    // 创建cell
                    cell = row.createCell(column);
                    cell.setCellStyle(this.styles.get(StrUtil.format("data_{}_{}_{}", fieldInfo.getAlign(), fieldInfo.getColor(), fieldInfo.getBackgroundColor())));

                    // 用于读取对象中的属性
                    Object value = getTargetValue(vo, fieldInfo.getFields());
                    String dateFormat = fieldInfo.getDateFormat();
                    String separator = fieldInfo.getSeparator();
                    if (StrUtil.isNotEmpty(dateFormat) && ObjectUtil.isNotNull(value)) {
                        cell.setCellValue(parseDateToStr(dateFormat, value));
                    } else if (CollUtil.isNotEmpty(fieldInfo.getReadConverterExp()) && ObjectUtil.isNotNull(value)) {
                        cell.setCellValue(FieldInfo.ModelInfo.convertByExp(ConvertUtil.toStr(value), fieldInfo.getReadConverterExp(), separator));
                    } else if (value instanceof BigDecimal && -1 != fieldInfo.getScale()) {
                        cell.setCellValue((((BigDecimal) value).setScale(fieldInfo.getScale(), fieldInfo.getRoundingMode())).doubleValue());
                    } else if (!fieldInfo.getHandler().equals(ExcelHandlerAdapter.class)) {
                        cell.setCellValue(dataFormatHandlerAdapter(wb, value, fieldInfo, cell));
                    } else {
                        // 设置列类型
                        setCellVo(value, fieldInfo, cell);
                    }
                    addStatisticsData(column, ConvertUtil.toStr(value), fieldInfo);
                } catch (Exception e) {
                    log.error("导出Excel失败{}", e);
                }
                return cell;
            }

            /**
             * 获取bean中的属性值
             *
             * @param vo     实体对象
             * @param fields 字段Field集合
             * @return 最终的属性值
             */
            @SneakyThrows
            private Object getTargetValue(C vo, List<Field> fields) {
                Object o = null;
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    field.setAccessible(true);
                    if (i == 0) {
                        o = field.get(vo);
                    } else {
                        o = field.get(o);
                    }
                }
                return o;
            }

            /**
             * 格式化不同类型的日期对象
             *
             * @param dateFormat 日期格式
             * @param val        被格式化的日期对象
             * @return 格式化后的日期字符
             */
            private String parseDateToStr(String dateFormat, Object val) {
                if (val == null) {
                    return StrUtil.EMPTY;
                }
                String str;
                if (val instanceof Date) {
                    str = DateUtil.parseDateToStr(dateFormat, (Date) val);
                } else if (val instanceof LocalDateTime) {
                    str = DateUtil.parseDateToStr(dateFormat, DateUtil.toDate((LocalDateTime) val));
                } else if (val instanceof LocalDate) {
                    str = DateUtil.parseDateToStr(dateFormat, DateUtil.toDate((LocalDate) val));
                } else {
                    str = val.toString();
                }
                return str;
            }

            /**
             * 数据处理器
             *
             * @param wb        工作薄对象
             * @param value     数据值
             * @param fieldInfo 字段信息对象
             * @param cell      单元格
             * @return 字符串
             */
            private String dataFormatHandlerAdapter(Workbook wb, Object value, FieldInfo fieldInfo, Cell cell) {
                try {
                    Object instance = fieldInfo.getHandler().getDeclaredConstructor().newInstance();
                    Method formatMethod = fieldInfo.getHandler().getMethod("format", Object.class, String[].class, Cell.class, Workbook.class);
                    value = formatMethod.invoke(instance, value, fieldInfo.getArgs(), cell, wb);
                } catch (Exception e) {
                    log.error("不能格式化数据{}{} ", fieldInfo.getHandler(), e.getMessage());
                }
                return ConvertUtil.toStr(value);
            }

            /**
             * 设置单元格信息
             *
             * @param value     单元格值
             * @param fieldInfo 字段信息对象
             * @param cell      单元格信息
             */
            private void setCellVo(Object value, FieldInfo fieldInfo, Cell cell) {
                if (ColumnType.STRING == fieldInfo.getCellType()) {
                    String cellValue = ConvertUtil.toStr(value);
                    // 对于任何以表达式触发字符 =-+@开头的单元格，直接使用tab字符作为前缀，防止CSV注入。
                    if (StrUtil.startWithAny(cellValue, FORMULA_STR)) {
                        cellValue = RegExUtils.replaceFirst(cellValue, FORMULA_REGEX_STR, "\t$0");
                    }
                    if (value instanceof Collection && StrUtil.equals(StrUtil.BRACKET, cellValue)) {
                        cellValue = StrUtil.EMPTY;
                    }
                    cell.setCellValue(ObjectUtil.isNull(cellValue) ? fieldInfo.getDefaultValue() : cellValue + fieldInfo.getSuffix());
                } else if (ColumnType.NUMERIC == fieldInfo.getCellType()) {
                    if (ObjectUtil.isNotNull(value)) {
                        cell.setCellValue(StrUtil.contains(ConvertUtil.toStr(value), StrUtil.DOT) ? ConvertUtil.toDouble(value) : ConvertUtil.toInt(value));
                    }
                } else if (ColumnType.IMAGE == fieldInfo.getCellType()) {
                    ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) cell.getColumnIndex(), cell.getRow()
                            .getRowNum(), (short) (cell.getColumnIndex() + 1), cell.getRow().getRowNum() + 1);
                    String imagePath = ConvertUtil.toStr(value);
                    if (StrUtil.isNotEmpty(imagePath)) {
                        byte[] data = ImageUtil.getImage(imagePath);
                        getDrawingPatriarch(cell.getSheet()).createPicture(anchor,
                                cell.getSheet().getWorkbook().addPicture(data, getImageType(data)));
                    }
                }
            }

            /**
             * 获取画布
             */
            private static Drawing<?> getDrawingPatriarch(Sheet sheet) {
                if (sheet.getDrawingPatriarch() == null) {
                    sheet.createDrawingPatriarch();
                }
                return sheet.getDrawingPatriarch();
            }

            /**
             * 获取图片类型,设置图片插入类型
             */
            private int getImageType(byte[] value) {
                String type = FileTypeUtil.getFileExtendName(value);
                if ("JPG".equalsIgnoreCase(type)) {
                    return Workbook.PICTURE_TYPE_JPEG;
                } else if ("PNG".equalsIgnoreCase(type)) {
                    return Workbook.PICTURE_TYPE_PNG;
                }
                return Workbook.PICTURE_TYPE_JPEG;
            }

            /**
             * 合计统计信息
             *
             * @param fieldInfo 字段信息对象
             */
            private void addStatisticsData(Integer index, String text, FieldInfo fieldInfo) {
                if (ObjectUtil.isNotNull(fieldInfo) && fieldInfo.getIsStatistics()) {
                    Double temp = 0D;
                    if (!this.statistics.containsKey(index)) {
                        this.statistics.put(index, temp);
                    }
                    try {
                        temp = Double.valueOf(text);
                    } catch (NumberFormatException ignored) {
                    }
                    this.statistics.put(index, this.statistics.get(index) + temp);
                }
            }

            /**
             * 创建统计行
             */
            private void addStatisticsRow() {
                if (MapUtil.isNotEmpty(this.statistics)) {
                    Row row = this.sheet.createRow(this.sheet.getLastRowNum() + 1);
                    Set<Integer> keys = this.statistics.keySet();
                    Cell cell = row.createCell(0);
                    cell.setCellStyle(this.styles.get("total"));
                    cell.setCellValue("合计");

                    for (Integer key : keys) {
                        cell = row.createCell(key);
                        cell.setCellStyle(this.styles.get("total"));
                        cell.setCellValue(DOUBLE_FORMAT.format(this.statistics.get(key)));
                    }
                    this.statistics.clear();
                }
            }


            /**
             * 对excel表单转换成list
             *
             * @param sheet 工作表对象
             * @return 转换后集合
             */
            @SneakyThrows
            private List<C> importExcel(Sheet sheet) {
                this.type = ExcelType.IMPORT;
                List<C> list = new ArrayList<>();

                // 获取最后一个非空行的行下标，比如总行数为n，则返回的为n-1
                int rows = sheet.getLastRowNum();

                if (rows > 0) {
                    // 定义一个map用于存放excel列的序号和FieldInfo
                    Map<String, Integer> cellMap = new HashMap<>();
                    // 获取表头
                    Row heard = sheet.getRow(this.titleNum);
                    for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++) {
                        Cell cell = heard.getCell(i);
                        if (ObjectUtil.isNotNull(cell)) {
                            String value = this.getCellValue(heard, i).toString();
                            cellMap.put(value, i);
                        } else {
                            cellMap.put(null, i);
                        }
                    }
                    // 有数据时才处理 得到类的所有FieldInfo
                    Map<Integer, FieldInfo> fieldsMap = new HashMap<>();
                    this.fieldList.forEach(item -> {
                        Integer index = cellMap.get(item.getName());
                        if (ObjectUtil.isNull(index)) {
                            throw new UtilException("模板异常，未识别到{}字段", item.getName());
                        }
                        fieldsMap.put(index, item);
                    });
                    for (int i = this.titleNum + 1; i <= rows; i++) {
                        // 从第2行开始取数据,默认第一行是表头
                        Row row = sheet.getRow(i);
                        // 判断当前行是否是空行
                        if (isRowEmpty(row)) {
                            continue;
                        }
                        C entity = null;
                        for (Map.Entry<Integer, FieldInfo> entry : fieldsMap.entrySet()) {
                            Object val = this.getCellValue(row, entry.getKey());

                            // 如果不存在实例则新建
                            entity = ObjectUtil.isNull(entity) ? clazz.getDeclaredConstructor().newInstance() : entity;
                            // 从map中得到对应列的field.
                            FieldInfo fieldInfo = entry.getValue();
                            // 取得类型,并根据对象类型设置值
                            getValueByFieldInfo(fieldInfo, entity, val, 0);
                        }
                        list.add(entity);
                    }
                }
                return list;
            }

            /**
             * 获取单元格值
             *
             * @param row    获取的行
             * @param column 获取单元格列号
             * @return 单元格值
             */
            private Object getCellValue(Row row, int column) {
                if (ObjectUtil.isNull(row)) {
                    return row;
                }
                Object val = StrUtil.EMPTY;
                try {
                    Cell cell = row.getCell(column);
                    if (ObjectUtil.isNotNull(cell)) {
                        if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                            val = cell.getNumericCellValue();
                            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                                // POI Excel 日期格式转换
                                val = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) val);
                            } else {
                                if ((Double) val % 1 != 0) {
                                    val = new BigDecimal(val.toString());
                                } else {
                                    val = new DecimalFormat("0").format(val);
                                }
                            }
                        } else if (cell.getCellType() == CellType.STRING) {
                            val = cell.getStringCellValue();
                        } else if (cell.getCellType() == CellType.BOOLEAN) {
                            val = cell.getBooleanCellValue();
                        } else if (cell.getCellType() == CellType.ERROR) {
                            val = cell.getErrorCellValue();
                        }
                    }
                } catch (Exception e) {
                    return val;
                }
                return val;
            }

            /**
             * 判断是否是空行
             *
             * @param row 判断的行
             * @return 结果
             */
            private boolean isRowEmpty(Row row) {
                if (ObjectUtil.isNull(row)) {
                    return true;
                }
                for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (ObjectUtil.isNotNull(cell) && ObjectUtil.notEqual(CellType.BLANK, cell.getCellType())) {
                        return false;
                    }
                }
                return true;
            }

            /**
             * 根据字段信息转换数据值
             *
             * @param fieldInfo 字段信息对象
             * @param entity    数据对象
             * @param val       单元格数据
             * @param index     序号
             */
            @SneakyThrows
            private void getValueByFieldInfo(FieldInfo fieldInfo, Object entity, Object val, int index) {
                Field field = fieldInfo.getFields().get(index);

                // 取得类型,并根据对象类型设置值.
                Class<?> fieldType = field.getType();
                if (CollUtil.isNotEmpty(fieldInfo.getReadConverterExp())) {
                    val = FieldInfo.ModelInfo.reverseByExp(ConvertUtil.toStr(val), fieldInfo.getReadConverterExp(), fieldInfo.getSeparator());
                }
                if (String.class == fieldType) {
                    String s = ConvertUtil.toStr(val);
                    if (StrUtil.endWith(s, ".0")) {
                        val = StrUtil.subBefore(s, ".0");
                    } else {
                        String dateFormat = fieldInfo.getDateFormat();
                        if (StrUtil.isNotBlank(dateFormat)) {
                            val = parseDateToStr(dateFormat, val);
                        } else {
                            val = ConvertUtil.toStr(val);
                        }
                    }
                } else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StrUtil.isNumeric(ConvertUtil.toStr(val))) {
                    val = ConvertUtil.toInt(val);
                } else if ((Long.TYPE == fieldType || Long.class == fieldType) && StrUtil.isNumeric(ConvertUtil.toStr(val))) {
                    val = ConvertUtil.toLong(val);
                } else if (Double.TYPE == fieldType || Double.class == fieldType) {
                    val = ConvertUtil.toDouble(val);
                } else if (Float.TYPE == fieldType || Float.class == fieldType) {
                    val = ConvertUtil.toFloat(val);
                } else if (BigDecimal.class == fieldType) {
                    val = ConvertUtil.toBigDecimal(val);
                } else if (Date.class == fieldType || LocalDate.class == fieldType || LocalDateTime.class == fieldType) {
                    if (val instanceof String str) {
                        val = DateUtil.parseDate(str);
                    } else if (val instanceof Double number) {
                        val = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(number);
                    }
                    if (ObjectUtil.isNotNull(val)) {
                        if (LocalDate.class == fieldType) {
                            val = ((Date) val).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        } else if (LocalDateTime.class == fieldType) {
                            val = ((Date) val).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        }
                    }
                } else if (Boolean.TYPE == fieldType || Boolean.class == fieldType) {
                    val = ConvertUtil.toBool(val, false);
                }
                if (ClassUtil.isNormalClass(fieldType) && ClassUtil.isNotSimpleType(fieldType)) {
                    Object subEntity = ReflectUtil.getFieldValue(entity, field);
                    if (ObjectUtil.isNull(subEntity)) {
                        subEntity = fieldType.getDeclaredConstructor().newInstance();
                        ReflectUtil.setFieldValue(entity, field, subEntity);
                    }
                    getValueByFieldInfo(fieldInfo, subEntity, val, ++index);
                } else {
                    ReflectUtil.setFieldValue(entity, field, val);
                }
            }

            /**
             * 字段信息对象
             */
            @Data
            @NoArgsConstructor
            public static class FieldInfo {

                /** 数据字段名 */
                private String field;

                /** Excel中的名字 */
                private String name;

                /** excel中的排序 */
                private Integer sort = Integer.MAX_VALUE;

                /** 日期格式, 如: yyyy-MM-dd */
                private String dateFormat = "";

                /** 内容转换表达式 */
                private List<ModelInfo> readConverterExp;

                /** 分隔符，读取字符串组内容 */
                private String separator = ",";

                /** BigDecimal 精度 默认:-1(默认不开启BigDecimal格式化) */
                private Integer scale = -1;

                /** BigDecimal 舍入规则 默认:BigDecimal.ROUND_HALF_EVEN */
                private RoundingMode roundingMode = RoundingMode.HALF_UP;

                /** 在excel中每个列的高度 单位为字符 */
                private Double height = 14.0;

                /** 在excel中每个列的宽 单位为字符 */
                private Double width = 16.0;

                /** 文字后缀,如% 90 变成90% */
                private String suffix = "";

                /** 当值为空时,字段的默认值 */
                private String defaultValue = "";

                /** 提示信息 */
                private String prompt = "";

                /** 设置只能选择不能输入的列内容 */
                private List<ModelInfo> combo;

                /** 是否需要纵向合并单元格,应对需求:含有list集合单元格) */
                private Boolean needMerge = Boolean.FALSE;

                /** 是否自动统计数据,在最后追加一行统计数据总和 */
                private Boolean isStatistics = Boolean.FALSE;

                /** 字段类型（0数字 1字符串） */
                private ColumnType cellType = ColumnType.STRING;

                /** 列头背景色 */
                private IndexedColors headerBackgroundColor = IndexedColors.GREY_50_PERCENT;

                /** 列头字体颜色 */
                private IndexedColors headerColor = IndexedColors.WHITE;

                /** 单元格背景色 */
                private IndexedColors backgroundColor = IndexedColors.WHITE;

                /** 单元格字体颜色 */
                private IndexedColors color = IndexedColors.BLACK;

                /** 字段对齐方式（0：默认；1：靠左；2：居中；3：靠右） */
                private HorizontalAlignment align = HorizontalAlignment.CENTER;

                /** 自定义数据处理器 */
                private Class<?> handler = ExcelHandlerAdapter.class;

                /** 自定义数据处理器参数 */
                private String[] args = {};

                /** 自动计算 · 对应类型字段 */
                List<Field> fields;

                /** 子对象字段配置 */
                private List<FieldInfo> children;

                /**
                 * 初始化字段配置
                 *
                 * @param json excel配置JSON
                 */
                public FieldInfo(JSONObject json) {
                    Field[] fields = FieldInfo.class.getDeclaredFields();
                    for (Field field : fields) {
                        String fieldName = field.getName();
                        try {
                            field.setAccessible(Boolean.TRUE);
                            String value = json.getString(fieldName);
                            if (StrUtil.isNotBlank(value)) {
                                // 是否为集合类型
                                if (ClassUtil.isCollection(field.getType())) {
                                    Type type = field.getGenericType();
                                    ParameterizedType parameterizedType = (ParameterizedType) type;
                                    if (parameterizedType.getActualTypeArguments()[0] instanceof Class<?> collectionType) {
                                        if (ClassUtil.equals(ModelInfo.class, collectionType)) {
                                            JSONArray array = json.getJSONArray(fieldName);
                                            List<ModelInfo> modelList = array.toList(JSONObject.class).stream().map(ModelInfo::new).collect(Collectors.toList());
                                            field.set(this, modelList);
                                        } else if (ClassUtil.equals(FieldInfo.class, collectionType)) {
                                            JSONArray array = json.getJSONArray(fieldName);
                                            List<FieldInfo> fieldList = array.toList(JSONObject.class).stream().map(FieldInfo::new).sorted(Comparator.comparing(FieldInfo::getSort)).collect(Collectors.toList());
                                            field.set(this, fieldList);
                                        }
                                    }
                                }
                                // 常规类型
                                else if (ClassUtil.isSimpleType(field.getType())) {
                                    field.set(this, ConvertUtil.convert(field.getType(), value));
                                }
                                // 字段类型字段
                                else if (ClassUtil.equals(ColumnType.class, field.getType())) {
                                    field.set(this, ColumnType.getByCodeElseNull(value));
                                }
                                // 颜色类型字段
                                else if (ClassUtil.equals(IndexedColors.class, field.getType())) {
                                    field.set(this, IndexedColors.fromInt(json.getInteger(fieldName)));
                                }
                                // 字段对齐方式字段
                                else if (ClassUtil.equals(HorizontalAlignment.class, field.getType())) {
                                    field.set(this, HorizontalAlignment.forInt(json.getInteger(fieldName)));
                                }
                                // BigDecimal舍入规则字段
                                else if (ClassUtil.equals(RoundingMode.class, field.getType())) {
                                    field.set(this, RoundingMode.valueOf(json.getInteger(fieldName)));
                                }
                            }
                        } catch (Exception e) {
                            log.error("excel配置转换异常，{}字段存在不匹配，错误原因：{}", fieldName, e.getMessage());
                        }
                    }
                }

                /**
                 * 映射信息对象
                 */
                @Data
                @NoArgsConstructor
                public static class ModelInfo {

                    private String value;

                    private String label;

                    /**
                     * 初始化字段配置
                     *
                     * @param json excel配置JSON
                     */
                    public ModelInfo(JSONObject json) {
                        Field[] fields = ModelInfo.class.getDeclaredFields();
                        for (Field field : fields) {
                            String fieldName = field.getName();
                            try {
                                field.setAccessible(Boolean.TRUE);
                                String value = json.getString(fieldName);
                                if (StrUtil.isNotBlank(value) && ClassUtil.isSimpleType(field.getType())) {
                                    field.set(this, ConvertUtil.convert(field.getType(), value));
                                }
                            } catch (Exception e) {
                                log.error("excel配置转换异常，{}字段存在不匹配，错误原因：{}", fieldName, e.getMessage());
                            }
                        }
                    }

                    /**
                     * 解析导出值 0=男,1=女,2=未知
                     *
                     * @param propertyValue 参数值
                     * @param converterExp  翻译注解
                     * @param separator     分隔符
                     * @return 解析后值
                     */
                    private static String convertByExp(String propertyValue, List<ModelInfo> converterExp, String separator) {
                        List<String> strList = StrUtil.split(propertyValue, separator);
                        Map<String, String> converterMap = converterExp.stream().collect(Collectors.toMap(ModelInfo::getValue, ModelInfo::getLabel, (v1, v2) -> v1));
                        StringBuilder propertyString = new StringBuilder();
                        for (String item : strList) {
                            propertyString.append(converterMap.getOrDefault(StrUtil.cleanBlank(item), StrUtil.EMPTY));
                        }
                        return propertyString.toString();
                    }

                    /**
                     * 反向解析值 男=0,女=1,未知=2
                     *
                     * @param propertyValue 参数值
                     * @param converterExp  翻译注解
                     * @param separator     分隔符
                     * @return 解析后值
                     */
                    private static String reverseByExp(String propertyValue, List<ModelInfo> converterExp, String separator) {
                        Map<String, String> converterMap = converterExp.stream().collect(Collectors.toMap(ModelInfo::getLabel, ModelInfo::getValue, (v1, v2) -> v1));
                        List<String> strList = StrUtil.split(propertyValue, separator);
                        StringBuilder propertyString = new StringBuilder();
                        for (String item : strList) {
                            propertyString.append(converterMap.getOrDefault(StrUtil.cleanBlank(item), StrUtil.EMPTY));
                        }
                        return propertyString.toString();
                    }
                }
            }
        }
    }

    /** 字段类型 */
    @Getter
    @AllArgsConstructor
    enum ColumnType {

        NUMERIC(0, "数字"),
        STRING(1, "字符串"),
        IMAGE(2, "图片");

        private final int value;
        private final String info;

        public static ColumnType getByCodeElseNull(String code) {
            return EnumUtil.getByCodeElseNull(ColumnType.class, code);
        }
    }

    /** 导入导出类型 */
    @Getter
    @AllArgsConstructor
    enum ExcelType {
        EXPORT(0),
        IMPORT(1);

        private final int value;
    }
}

