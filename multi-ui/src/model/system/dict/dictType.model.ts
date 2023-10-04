import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '@/model';
import { DictDataIM } from '@/model/system/dict/index';
import { DicCacheTypeEnum, DicDataTypeEnum } from '@/enums/system/dict';
import { DicStatusEnum } from '@/enums';
import { EnterpriseIM } from '@/model/system/organize';

/** dictType info model */
export interface DictTypeIM extends SubBaseEntity<DictDataIM> {
  /** 字典Id */
  id: string;
  /** 字典名称 */
  name: string;
  /** 字典类型 */
  code: string;
  /** 数据类型（0默认 1只增 2只减 3只读） */
  dataType: DicDataTypeEnum;
  /** 缓存类型（0租户 1全局） */
  cacheType: DicCacheTypeEnum;
  /** 状态（0启用 1禁用） */
  status: DicStatusEnum;
  /** 企业Id */
  tenantId?: string;
  /** 企业信息 */
  enterpriseInfo?: EnterpriseIM;
}

/** dictType list model */
export type DictTypeLM = DictTypeIM[];

/** dictType param model */
export interface DictTypePM extends SubBaseEntity<DictDataIM> {
  /** 字典名称 */
  name?: string;
  /** 字典类型 */
  code?: string;
  /** 数据类型（0默认 1只增 2只减 3只读） */
  dataType?: DicDataTypeEnum;
  /** 缓存类型（0租户 1全局） */
  cacheType?: DicCacheTypeEnum;
  /** 状态（0启用 1禁用） */
  status?: DicStatusEnum;
  tenantId?: string;
  tenantIds?: string[];
}

/** dictType page param model */
export type DictTypePPM = BasicPageParams & DictTypePM;

/** dictType list result model */
export type DictTypeLRM = BasicFetchResult<DictTypeIM>;
