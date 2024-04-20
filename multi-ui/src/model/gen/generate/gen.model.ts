import { BaseEntity, BasicFetchResult, BasicPageParams, SubBaseEntity } from '@/model';

/** genTable item model */
export interface GenTableIM extends SubBaseEntity<GenTableColumnIM> {
  /** Id */
  id: string;
  /** 名称 */
  name: string;
  /** 表描述 */
  comment: string;
  /** 实体类名称(首字母大写) */
  className: string;
  /** 实体类名称前缀(首字母大写) */
  prefix: string;
  /** 使用的模板（base单表操作 tree树表操作） */
  tplCategory: string;
  /** 生成后端包路径 */
  rdPackageName: string;
  /** 生成权限名 */
  fePackageName: string;
  /** 生成模块路径 */
  moduleName: string;
  /** 生成业务名 */
  businessName: string;
  /** 生成权限标识 */
  authorityName: string;
  /** 生成功能名 */
  functionName: string;
  /** 生成作者 */
  functionAuthor: string;
  /** 生成路径类型（0默认路径 1自定义路径） */
  genType: string;
  /** 后端生成路径（不填默认项目路径） */
  genPath: string;
  /** 前端生成路径（不填默认项目路径） */
  uiPath: string;
  /** 其它生成选项 */
  options: string;
  type: string;
  subTableName: string;
  subTableFkName: string;
}

/** genTable list model */
export type GenTableLM = GenTableIM[];

/** genTable param model */
export interface GenTablePM extends SubBaseEntity<GenTableColumnIM> {
  /** Id */
  id?: string;
  /** 名称 */
  name?: string;
  /** 使用的模板（base单表操作 tree树表操作） */
  tplCategory?: string;
}

/** genTable page param model */
export type GenTablePPM = BasicPageParams & GenTablePM;

/** genTable list result model */
export type GenTableLRM = BasicFetchResult<GenTableIM>;

/** genTableColumn item model */
export interface GenTableColumnIM extends BaseEntity {
  id: string;
  tableId: string;
  name: string;
  comment: string;
  type: string;
  javaType: string;
  javaField: string;
  isPk: boolean;
  isIncrement: boolean;
  isRequired: boolean;
  isView: boolean;
  isInsert: boolean;
  isEdit: boolean;
  isList: boolean;
  isQuery: boolean;
  isImport: boolean;
  isExport: boolean;
  isCover: boolean;
  isHide: boolean;
  isUnique: boolean;
  queryType: string;
  htmlType: string;
  dictType: string;
  sort: number;
}

/** genTableColumn list model */
export type GenTableColumnLM = GenTableColumnIM[];

/** genTableColumn param model */
export type GenTableColumnPM = GenTableColumnIM;

/** genTableColumn page param model */
export type GenTableColumnPPM = BasicPageParams & GenTableColumnPM;

/** genTableColumn list result model */
export type GenTableColumnLRM = BasicFetchResult<GenTableColumnIM>;

/** gen code item model */
export interface GenCodeIM {
  name: string;
  language: string;
  template: string;
}

/** gen code list model */
export type GenCodeLM = GenCodeIM[];

/** option item model */
export interface OptionIM {
  isTenant: string;
  sourceMode: string;
  parentModuleId: string;
  parentMenuId: string;
  id: string;
  name: string;
  status: string;
  sort: string;
  treeCode: string;
  parentId: string;
  treeName: string;
  ancestors: string;
  foreignId: string;
  subTableId: string;
  subForeignId: string;
}
