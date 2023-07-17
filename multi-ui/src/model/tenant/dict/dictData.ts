import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/src';

/** dictData info model */
export interface DictDataIM extends BaseEntity {
  id: string;
  code: string;
  value: string;
  label: string;
  sort: number;
  isDefault: string;
  cssClass: string;
  listClass: string;
  status: string;
  remark: string;
}

/** dictData list model */
export type DictDataLM = DictDataIM[];

/** dictData param model */
export interface DictDataPM extends BaseEntity {
  id?: string;
  code?: string;
  value?: string;
  label?: string;
  isDefault?: string;
  cssClass?: string;
  listClass?: string;
  status?: string;
}

/** dictData page param model */
export type DictDataPPM = BasicPageParams & DictDataPM;

/** dictData list result model */
export type DictDataLRM = BasicFetchResult<DictDataIM>;
