import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '@/model/basic';
import { DictDataIM } from './dictData.model';

/** dictType info model */
export interface DictTypeIM extends SubBaseEntity<DictDataIM> {
  id: string;
  name: string;
  code: string;
  sort: number;
  status: string;
  remark: string;
}

/** dictType list model */
export type DictTypeLM = DictTypeIM[];

/** dictType param model */
export interface DictTypePM extends SubBaseEntity<DictDataIM> {
  id?: string;
  name?: string;
  code?: string;
  status?: string;
}

/** dictType page param model */
export type DictTypePPM = BasicPageParams & DictTypePM;

/** dictType list result model */
export type DictTypeLRM = BasicFetchResult<DictTypeIM>;
