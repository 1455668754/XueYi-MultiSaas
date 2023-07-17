export interface BasicPageParams {
  page: number;
  pageSize: number;
}

export interface BasicFetchResult<T> {
  items: T[];
  total: number;
}

export interface BaseEntity {
  createBy?: string;
  createName?: string;
  createTime?: string;
  updateBy?: string;
  updateName?: string;
  updateTime?: string;
  params?: {};
}

export interface SubBaseEntity<S> extends BaseEntity {
  subList?: S[];
}

export interface TreeEntity<T> extends BaseEntity {
  parentId?: string;
  parentName?: string;
  ancestors?: string;
  /** 是否存在默认顶级（true存在 false不存在） */
  defaultNode?: boolean;
  /** 移除当前及子节点（true是 false否） */
  exNodes?: boolean;
  /** 自定义顶级节点名称 */
  topNodeName?: string;
  children?: T[];
}

export interface SubTreeEntity<T, S> extends TreeEntity<T> {
  subList?: S[];
}
