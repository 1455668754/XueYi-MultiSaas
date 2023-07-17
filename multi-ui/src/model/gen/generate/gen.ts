import { BaseEntity, BasicFetchResult, BasicPageParams, SubBaseEntity } from '/@/model/src';

/** genTable item model */
export interface GenTableIM extends SubBaseEntity<GenTableColumnIM> {
  id: string;
  name: string;
  comment: string;
  type: string;
  subTableName: string;
  subTableFkName: string;
  className: string;
  prefix: string;
  tplCategory: string;
  packageName: string;
  moduleName: string;
  authorityName: string;
  businessName: string;
  functionName: string;
  functionAuthor: string;
  genType: string;
  genPath: string;
  uiPath: string;
  options: string;
}

/** genTable list model */
export type GenTableLM = GenTableIM[];

/** genTable param model */
export interface GenTablePM extends SubBaseEntity<GenTableColumnIM> {
  id?: string;
  name?: string;
  comment?: string;
  type?: string;
  subTableName?: string;
  subTableFkName?: string;
  className?: string;
  prefix?: string;
  tplCategory?: string;
  packageName?: string;
  moduleName?: string;
  authorityName?: string;
  businessName?: string;
  functionName?: string;
  functionAuthor?: string;
  genType?: string;
  genPath?: string;
  uiPath?: string;
  options?: string;
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
