export interface BasicPageParams {
  page: number;
  pageSize: number;
}

export interface BasicFetchResult<T> {
  items: T[];
  total: number;
}

export interface BaseEntity {
  createBy?: string | number;
  createName?: string;
  createTime?: string;
  updateBy?: string | number;
  updateName?: string;
  updateTime?: string;
  params?: {};
}

export interface SubBaseEntity<S> extends BaseEntity {
  subList?: S[];
}

export interface TreeEntity<T> extends BaseEntity {
  parentId: string | number;
  parentName?: string;
  ancestors?: string;
  children?: T[];
}

export interface SubTreeEntity<T, S> extends TreeEntity<T> {
  subList?: S[];
}
