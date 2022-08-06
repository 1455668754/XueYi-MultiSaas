import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '/@/model/src';
import { DictDataIM } from './dictData';

/** dictType info model */
export interface DictTypeIM extends SubBaseEntity<DictDataIM> {
  id: string | number;
  name: string;
  code: string;
  sort: number;
  status: string;
  remark: string;
}

/** dictType list model */
export type DictTypeLM = DictTypeIM[];

/** dictType param model */
export type DictTypePM = DictTypeIM;

/** dictType page param model */
export type DictTypePPM = BasicPageParams & DictTypePM;

/** dictType list result model */
export type DictTypeLRM = BasicFetchResult<DictTypeIM>;
