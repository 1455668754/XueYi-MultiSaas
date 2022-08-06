import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** dictData info model */
export interface DictDataIM extends BaseEntity {
  id: string | number;
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
export type DictDataPM = DictDataIM;

/** dictData page param model */
export type DictDataPPM = BasicPageParams & DictDataPM;

/** dictData list result model */
export type DictDataLRM = BasicFetchResult<DictDataIM>;
