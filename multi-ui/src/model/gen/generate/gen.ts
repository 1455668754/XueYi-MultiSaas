import { BaseEntity, SubBaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/src';

/** genTable item model */
export interface GenTableIM extends SubBaseEntity<GenTableColumnIM> {
  id: string | number;
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
export type GenTablePM = GenTableLM;

/** genTable page param model */
export type GenTablePPM = BasicPageParams & GenTablePM;

/** genTable list result model */
export type GenTableLRM = BasicFetchResult<GenTableIM>;

/** genTableColumn item model */
export interface GenTableColumnIM extends BaseEntity {
  id: string | number;
  tableId: string | number;
  name: string;
  comment: string;
  type: string;
  javaType: string;
  javaField: string;
  pk: boolean;
  increment: boolean;
  required: boolean;
  view: boolean;
  insert: boolean;
  edit: boolean;
  list: boolean;
  query: boolean;
  import: boolean;
  export: boolean;
  cover: boolean;
  hide: boolean;
  unique: boolean;
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
  parentModuleId: string | number;
  parentMenuId: string | number;
  id: string | number;
  name: string | number;
  status: string | number;
  sort: string | number;
  treeCode: string | number;
  parentId: string | number;
  treeName: string | number;
  ancestors: string | number;
  foreignId: string | number;
  subTableId: string | number;
  subForeignId: string | number;
}
