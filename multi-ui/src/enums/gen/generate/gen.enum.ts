/** 代码生成主页路由 */
export const GenIndexGo = 'ebd46c2fd3c3429896de95a82bcf1d8b';

/** 代码生成编辑页路由 */
export const GenGenerateDetailGo = 'b489b7b0e645471eb42ed8b1f0365d32';

/** 代码生成预览页路由 */
export const GenCodeDetailGo = 'c49d2b1d7d6640e7bd331aa494b05e58';

/** 字典编码：字典 */
export enum DicCodeGenEnum {
  // 代码生成：模板类型
  GEN_TEMPLATE_TYPE = 'gen_template_type',
  // 代码生成：属性类型
  GEN_JAVA_TYPE = 'gen_java_type',
  // 代码生成：查询类型
  GEN_QUERY_TYPE = 'gen_query_type',
  // 代码生成：显示类型
  GEN_DISPLAY_TYPE = 'gen_display_type',
  // 代码生成：生成路径
  GEN_GENERATION_MODE = 'gen_generation_mode',
  // 代码生成-源策略模式
  GEN_SOURCE_MODE = 'gen_source_mode',
}

/** 字典：表模板类型 */
export enum TemplateTypeEnum {
  // 单表
  BASE = 'base',
  // 树表
  TREE = 'tree',
  // 关联表
  MERGE = 'merge',
}

/** 字典：状态（Y是 N否） */
export enum GenStatusEnum {
  // 是
  TRUE = 'Y',
  // 否
  FALSE = 'N',
}

/** 字典：生成方式 */
export enum GenerationModeEnum {
  // zip压缩包
  ZIP = '0',
  // 自定义路径
  CUSTOM = '1',
}

/** 字典：源策略 */
export enum SourceModeEnum {
  // 主数据源
  MASTER = 'Master',
  // 策略源
  ISOLATE = 'Isolate',
}

/** 是否勾选 */
export enum IsTickEnum {
  YES = '√',
  NO = '×',
}
